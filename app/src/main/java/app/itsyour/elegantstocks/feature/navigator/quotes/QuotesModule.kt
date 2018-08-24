package app.itsyour.elegantstocks.feature.navigator.quotes

import app.itsyour.elegantstocks.application.dependency.scope.FragmentScoped
import app.itsyour.elegantstocks.feature.symbol.SymbolContract
import app.itsyour.elegantstocks.feature.symbol.SymbolPresenter
import dagger.Binds
import dagger.android.ContributesAndroidInjector

@dagger.Module
abstract class QuotesModule {

    @Binds
    abstract fun bindQuotes(presenter: QuotesPresenter): QuotesContract.Presenter

    @Binds
    abstract fun bindSymbol(presenter: SymbolPresenter): SymbolContract.Presenter

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun quotesFragment(): QuotesFragment

//    @dagger.Module
//    companion object {
//        private const val ALPHA_VANTAGE_ENDPOINT = "https://api.iextrading.com/"
//
//        @ActivityScoped
//        @Provides @JvmStatic
//        fun quoteApi(builder: Retrofit.Builder, client: OkHttpClient): QuotesApi {
//            return builder
//                .client(client)
//                .baseUrl(ALPHA_VANTAGE_ENDPOINT)
//                .build()
//                .create<QuotesApi>(QuotesApi::class.java)
//        }
//
//        @ActivityScoped
//        @Provides @JvmStatic
//        fun referenceApi(builder: Retrofit.Builder, client: OkHttpClient): SymbolApi {
//            return builder
//                    .client(client)
//                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
//                    .build()
//                    .create(SymbolApi::class.java)
//        }
//
//        @ActivityScoped
//        @Provides @JvmStatic
//        fun chartApi(builder: Retrofit.Builder, client: OkHttpClient): ChartsApi {
//            return builder
//                    .client(client)
//                    .baseUrl(ALPHA_VANTAGE_ENDPOINT)
//                    .build()
//                    .create(ChartsApi::class.java)
//        }
//    }
}