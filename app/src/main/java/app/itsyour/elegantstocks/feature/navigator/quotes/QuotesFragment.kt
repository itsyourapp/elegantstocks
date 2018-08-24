package app.itsyour.elegantstocks.feature.navigator.quotes

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.base.BaseFragment
import app.itsyour.elegantstocks.feature.navigator.Navigator
import app.itsyour.elegantstocks.feature.navigator.NavigatorActivity
import app.itsyour.elegantstocks.feature.navigator.quotes.detail.QuoteDetailActivity
import app.itsyour.elegantstocks.views.LoadingFrame
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_quotes.*

class QuotesFragment : BaseFragment<QuotesContract.Presenter>(), QuotesContract.View {

    companion object {
        const val FRAGMENT_TAG = "quotesFragment"
        fun newInstance(): QuotesFragment = QuotesFragment()
    }

    private val actionsSubject = PublishRelay.create<QuotesContract.Action>()
    override val actions: Observable<QuotesContract.Action>
        get() = actionsSubject.mergeWith(
                adapter.selected.map { QuotesContract.Action.Select(it) })

    private var navigator: Navigator? = null

    private val adapter = QuotesAdapter()

    override fun getLayoutId(): Int = R.layout.fragment_quotes

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigator = (context as? Navigator)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSymbolsList()
    }

    private fun setupSymbolsList() {
        val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        quotes_symbolsList.layoutManager = layoutManager
        quotes_symbolsList.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listenOnNavigator()
        showFab()
    }

    private fun listenOnNavigator() {
        val actions = navigator?.actions
        if (actions != null)
            subscriptions += actions.subscribe(this::onNavigatorActionReceived)
    }

    private fun onNavigatorActionReceived(action: Navigator.Action) {
        when (action) {
            is Navigator.Action.Refresh -> actionsSubject.accept(QuotesContract.Action.Refresh())
        }
    }

    private fun showFab() {
        (activity as NavigatorActivity).showFab()
    }

    override fun onStateReceived(state: QuotesContract.State) {
        when (state) {
            is QuotesContract.State.Refreshed -> onRefreshed()
            is QuotesContract.State.Snapshots -> {
                navigator?.onStateReceived(Navigator.State.Refreshed())
                navigator?.onStateReceived(Navigator.State.Refreshable(true))
                display(state.snapshots)
            }
            is QuotesContract.State.NoSymbols -> {
                navigator?.onStateReceived(Navigator.State.Refreshed())
                navigator?.onStateReceived(Navigator.State.Refreshable(false))
                displayNoWatching()
            }
            is QuotesContract.State.Error -> {
                quotes_loadingFrame.setState(LoadingFrame.ERROR)
                navigator?.onStateReceived(Navigator.State.Refreshed())
                navigator?.onStateReceived(Navigator.State.Refreshable(true))
                displayError()
            }
            is QuotesContract.State.Navigate -> {
                when (state) {
                    is QuotesContract.State.Navigate.Details ->
                        QuoteDetailActivity.navigateTo(context!!, state.snapshot.symbol.symbol)
                }
            }
        }
    }

    private fun onRefreshed() {
        navigator?.onStateReceived(Navigator.State.Refreshed())
    }

    private fun display(symbolsWithQuotes: List<QuotesContract.Snapshot>) {
        adapter.snapshots = symbolsWithQuotes.sortedBy { it.symbol.symbol }
        quotes_loadingFrame.setState(LoadingFrame.SUCCESS)
    }

    private fun displayNoWatching() {
        quotes_loadingFrame.setErrorText(R.string.error_nosymbols)
        quotes_loadingFrame.setState(LoadingFrame.ERROR)
    }

    private fun displayError() {
        quotes_loadingFrame.setErrorText(R.string.error_generic)
        quotes_loadingFrame.setState(LoadingFrame.ERROR)
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }
}
