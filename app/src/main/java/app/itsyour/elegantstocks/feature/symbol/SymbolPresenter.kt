package app.itsyour.elegantstocks.feature.symbol

import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.feature.base.BasePresenter
import app.itsyour.elegantstocks.feature.navigator.quotes.QuotesInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

class SymbolPresenter
    @Inject constructor(
        private val quotesInteractor: QuotesInteractor,
        private val repository: MarketRepository)
                : BasePresenter<SymbolContract.View>(),
                  SymbolContract.Presenter {

    private val symbols by lazy { quotesInteractor.symbols.cache() }

    override fun onViewTaken() {
        subscriptions += view.actions.flatMap(::onActionReceived)
                .startWith(SymbolContract.State.Loading())
                .mergeWith(symbols())
                .onErrorReturnItem(SymbolContract.State.Error())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::onStateReceived)
    }

    private fun symbols(): Single<SymbolContract.State> =
           symbols.map<SymbolContract.State> { SymbolContract.State.Symbols(it) }
                .onErrorReturnItem(SymbolContract.State.Error(
                        SymbolContract.State.Error.Type.Network))


    private fun onActionReceived(action: SymbolContract.Action) : Observable<SymbolContract.State> =
        when (action) {
            is SymbolContract.Action.Query -> querySymbols(action.text)
            is SymbolContract.Action.Add -> {
                repository.insert(action.symbol)
                Observable.just(SymbolContract.State.Navigation(
                        SymbolContract.State.Navigation.Route.Quotes))
            }
    }

    private fun querySymbols(query: String): Observable<SymbolContract.State> =
        symbols.flatMapObservable { it.toObservable() }
                .filter {
                    it.symbol.contains(query, true)
                    || it.name.contains(query, true) }
                .toList()
                .flatMapObservable {(
                     if (it.isEmpty()) SymbolContract.State.Error(
                        SymbolContract.State.Error.Type.EmptyList)
                     else SymbolContract.State.Symbols(it))
                        .let { Observable.just(it) }
        }
}