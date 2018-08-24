package app.itsyour.elegantstocks.feature

import app.itsyour.elegantstocks.application.dependency.scope.ActivityScoped
import app.itsyour.elegantstocks.application.dependency.scope.AppScoped
import app.itsyour.elegantstocks.feature.chart.candle.CandleActivity
import app.itsyour.elegantstocks.feature.chart.candle.CandleModule
import app.itsyour.elegantstocks.feature.chart.model.ChartsApi
import app.itsyour.elegantstocks.feature.navigator.NavigatorActivity
import app.itsyour.elegantstocks.feature.navigator.NavigatorModule
import app.itsyour.elegantstocks.feature.navigator.quotes.QuotesModule
import app.itsyour.elegantstocks.feature.navigator.quotes.detail.QuoteDetailActivity
import app.itsyour.elegantstocks.feature.navigator.quotes.detail.QuoteDetailContract
import app.itsyour.elegantstocks.feature.navigator.quotes.detail.QuoteDetailPresenter
import app.itsyour.elegantstocks.feature.navigator.quotes.model.QuotesApi
import app.itsyour.elegantstocks.feature.symbol.SymbolActivity
import app.itsyour.elegantstocks.feature.symbol.model.SymbolApi
import dagger.Binds
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@dagger.Module
abstract class FeatureModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [NavigatorModule::class])
    abstract fun navigator(): NavigatorActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [QuotesModule::class])
    abstract fun symbol(): SymbolActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun detailActivity(): QuoteDetailActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CandleModule::class])
    abstract fun chartActivity(): CandleActivity

    @dagger.Module
    companion object {
        private const val ALPHA_VANTAGE_ENDPOINT = "https://api.iextrading.com/"

        @AppScoped
        @Provides @JvmStatic
        fun quoteApi(builder: Retrofit.Builder, client: OkHttpClient): QuotesApi {
            return builder
                    .client(client)
                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
                    .build()
                    .create<QuotesApi>(QuotesApi::class.java)
        }

        @AppScoped
        @Provides @JvmStatic
        fun referenceApi(builder: Retrofit.Builder, client: OkHttpClient): SymbolApi {
            return builder
                    .client(client)
                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
                    .build()
                    .create(SymbolApi::class.java)
        }

        @AppScoped
        @Provides @JvmStatic
        fun chartApi(builder: Retrofit.Builder, client: OkHttpClient): ChartsApi {
            return builder
                    .client(client)
                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
                    .build()
                    .create(ChartsApi::class.java)
        }
    }

    @Binds
    abstract fun bindDetail(presenter: QuoteDetailPresenter): QuoteDetailContract.Presenter
}