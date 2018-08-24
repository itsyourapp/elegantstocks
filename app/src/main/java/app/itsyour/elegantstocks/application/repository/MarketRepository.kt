package app.itsyour.elegantstocks.application.repository

import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import app.itsyour.elegantstocks.feature.navigator.quotes.model.QuoteDao
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import app.itsyour.elegantstocks.feature.symbol.model.SymbolDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * A simple repository.
 *
 * TODO: Store with Room library
 */
class MarketRepository(
        permanent: PermanentDatabase,
        transient: TransientDatabase) {

    private var symbolDao: SymbolDao = permanent.symbolDao()
    private var quoteDao: QuoteDao = transient.quoteDao()

    var allQuotes: Observable<List<Quote>>
            = quoteDao.fetchAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    fun insertAll(quotes: List<Quote>) {
        Completable.fromCallable { quoteDao.insertAll(quotes) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    var allSymbols: Flowable<List<Symbol>>
            = symbolDao.fetchAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    fun insert(symbol: Symbol) {
        Completable.fromCallable { symbolDao.insert(symbol) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun delete(symbol: String) {
        Completable.fromCallable { symbolDao.delete(symbol) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun delete(symbol: Symbol) {
        Completable.fromCallable { symbolDao.delete(symbol) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}