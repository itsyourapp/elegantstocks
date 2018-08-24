package app.itsyour.elegantstocks.feature.navigator.quotes

import app.itsyour.elegantstocks.application.dependency.scope.ActivityScoped
import app.itsyour.elegantstocks.feature.chart.model.ChartsInteractor
import app.itsyour.elegantstocks.feature.navigator.quotes.model.QuotesApi
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import app.itsyour.elegantstocks.feature.symbol.model.SymbolApi
import app.itsyour.elegantstocks.feature.symbol.model.toApiString
import com.github.mikephil.charting.data.Entry
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

@ActivityScoped
class QuotesInteractor
    @Inject constructor(
            private val quotesApi: QuotesApi,
            private val symbolApi: SymbolApi,
            private val chartsInteractor: ChartsInteractor) {

    val symbols: Single<List<Symbol>>
        get() = symbolApi.symbols

    fun fetchSnapshots(symbols: List<Symbol>): Single<List<QuotesContract.Snapshot>> {
        return quotesApi.fetchQuotes(symbols.toApiString())
            .flatMap { map -> Single.just(map.mapKeys { entry -> symbols.find { it.symbol == entry.key }!! }.toList()) }
            .flatMapObservable { it.toObservable() }
            .flatMapSingle { pair -> fetchChart(pair.first)
                    .map { QuotesContract.Snapshot(pair.first, pair.second.quote, it) } }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchChart(symbol: Symbol): Single<List<Entry>> {
        return chartsInteractor
                .fetchChart(symbol.symbol, ChartsInteractor.Companion.Range.ONE_MONTH)
                .flatMapIterable { it }
                .takeLast(5)
                .map { Entry(it.date.time.toFloat(), it.close.toFloat()) }
                .toList()
    }
}
