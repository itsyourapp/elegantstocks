package app.itsyour.elegantstocks.feature.navigator.quotes

import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.feature.base.BasePresenter
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import io.reactivex.rxkotlin.plusAssign
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class QuotesPresenter
    @Inject constructor(
            private val quotesInteractor: QuotesInteractor,
            private val repository: MarketRepository)
                : BasePresenter<QuotesContract.View>(), QuotesContract.Presenter {

    private var favoritedSymbols = repository.allSymbols.replay(1).refCount()

    override fun onViewTaken() {
        subscribeToFavorites()
        listenForActions(view)
    }

    private fun subscribeToFavorites() {
        subscriptions += favoritedSymbols.subscribe(::onSymbolsReceived)
    }

    private fun onSymbolsReceived(symbols: List<Symbol>) {
        if (symbols.isNotEmpty())
            subscriptions += quotesInteractor.fetchSnapshots(symbols)
                     .doFinally { view.onStateReceived(QuotesContract.State.Refreshed()) }
                     .subscribe(this::onSnapshotsReceived, this::onSnapshotsError)
        else
            view.onStateReceived(QuotesContract.State.NoSymbols())
    }

    private fun onSnapshotsReceived(snapshots: List<QuotesContract.Snapshot>) {
        repository.insertAll(snapshots.map { it.quote })
        view.onStateReceived(QuotesContract.State.Snapshots(snapshots))
    }

    private fun onSnapshotsError(error: Throwable) {
        // TODO: Handle multiple error states
        when (error) {
            is HttpException,
            is UnknownHostException -> {
                view.onStateReceived(QuotesContract.State.Error())
            }
        }
    }

    private fun listenForActions(view: QuotesContract.View) {
        subscriptions += view.actions.subscribe(::onActionReceived)
    }

    private fun onActionReceived(action: QuotesContract.Action) {
        when (action) {
            is QuotesContract.Action.Select ->
                view.onStateReceived(QuotesContract.State.Navigate.Details(action.snapshot))
            is QuotesContract.Action.Refresh -> refresh()
        }
    }

    private fun refresh() {
        val mostRecentSymbols = favoritedSymbols.blockingFirst()
        if (mostRecentSymbols.isNotEmpty())
            onSymbolsReceived(mostRecentSymbols)
        else
            view.onStateReceived(QuotesContract.State.Refreshed())
    }
}