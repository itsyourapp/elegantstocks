package app.itsyour.elegantstocks.feature.navigator.news

import app.itsyour.elegantstocks.application.dependency.scope.ActivityScoped
import app.itsyour.elegantstocks.application.dependency.scope.FragmentScoped
import app.itsyour.elegantstocks.feature.navigator.news.model.NewsApi
import dagger.Binds
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@dagger.Module
abstract class NewsModule {

    @Binds
    abstract fun bindsPresenter(presenter: NewsPresenter): NewsContract.Presenter

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newsFragment(): NewsFragment

    @dagger.Module
    companion object {
        private const val ALPHA_VANTAGE_ENDPOINT = "https://api.iextrading.com/"

        @ActivityScoped
        @Provides @JvmStatic
        fun newsApi(builder: Retrofit.Builder, client: OkHttpClient): NewsApi {
            return builder
                    .client(client)
                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
                    .build()
                    .create<NewsApi>(NewsApi::class.java)
        }
    }
}