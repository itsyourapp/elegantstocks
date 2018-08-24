package app.itsyour.elegantstocks.application.dependency

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import app.itsyour.elegantstocks.BuildConfig
import app.itsyour.elegantstocks.application.dependency.scope.AppScoped
import app.itsyour.elegantstocks.application.network.CacheInterceptor
import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.application.repository.PermanentDatabase
import app.itsyour.elegantstocks.application.repository.TransientDatabase
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory.create
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Provides dependencies for the application dependency graph across the
 * lifetime of the application.
 */
@dagger.Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context

    @dagger.Module
    companion object {
        @AppScoped
        @Provides @JvmStatic
        fun sharedPreferences(context: Context): SharedPreferences
                = context.getSharedPreferences(context.packageName, MODE_PRIVATE)

        @AppScoped
        @Provides @JvmStatic
        fun gson(): Gson
                = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

        @AppScoped
        @Provides @JvmStatic
        fun rxJavaCallAdapterFactory(): RxJava2CallAdapterFactory
                = RxJava2CallAdapterFactory.createAsync()

        @AppScoped
        @Provides @JvmStatic
        fun gsonConverterFactory(gson: Gson): GsonConverterFactory
                = create(gson)

        @AppScoped
        @Provides @JvmStatic
        fun cache(context: Context): Cache {
            val httpCacheDirectory = File(context.cacheDir, "responses")
            val cacheSize = 10 * 1024 * 1024 // 10 MiB
            return Cache(httpCacheDirectory, cacheSize.toLong())
        }

        @AppScoped
        @Provides @JvmStatic
        fun retrofitBuilder(converterFactory: GsonConverterFactory, callFactory: RxJava2CallAdapterFactory): Retrofit.Builder {
            return Retrofit.Builder()
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(callFactory)
        }

        @AppScoped
        @Provides @JvmStatic
        fun httpClient(cache: Cache): OkHttpClient {
            val builder = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addNetworkInterceptor(CacheInterceptor())
                    .cache(cache)

            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(StethoInterceptor())
                @Suppress("ConstantConditionIf")
                if ("debug" == BuildConfig.BUILD_TYPE) {
                    builder.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            return builder.build()
        }

        @AppScoped
        @Provides @JvmStatic
        fun permanentDatabase(context: Context): PermanentDatabase =
            Room.databaseBuilder(
                context.applicationContext, PermanentDatabase::class.java, "permanent.db")
                    .build()

        @AppScoped
        @Provides @JvmStatic
        fun transientDatabase(context: Context): TransientDatabase =
                Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        TransientDatabase::class.java)
                                .build()

        @AppScoped
        @Provides @JvmStatic
        fun marketRepository(
                permanent: PermanentDatabase,
                transient: TransientDatabase) =
                    MarketRepository(permanent, transient)
    }
}