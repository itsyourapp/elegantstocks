package app.itsyour.elegantstocks.feature.navigator.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.base.BaseFragment
import app.itsyour.elegantstocks.feature.navigator.Navigator
import app.itsyour.elegantstocks.feature.navigator.NavigatorActivity
import app.itsyour.elegantstocks.feature.navigator.news.model.MarketNews
import app.itsyour.elegantstocks.views.LoadingFrame
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : BaseFragment<NewsContract.Presenter>(), NewsContract.View {

    companion object {
        const val FRAGMENT_TAG = "quotesFragment"
        fun newInstance() = NewsFragment()
    }

    private val actionsSubject = PublishRelay.create<NewsContract.Action>()
    override val actions: Observable<NewsContract.Action>
        get() = actionsSubject.mergeWith(adapter.selectedMarketNews
                .map { NewsContract.Action.Select(it) })

    private var navigator: Navigator? = null
    private val adapter = NewsAdapter()

    override fun getLayoutId() = R.layout.fragment_news

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigator = (context as? Navigator)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listenOnNavigator()
        hideFab()
    }

    private fun listenOnNavigator() {
        val actions = navigator?.actions
        if (actions != null)
            subscriptions += actions.subscribe(this::onNavigatorActionReceived)
    }

    private fun onNavigatorActionReceived(action: Navigator.Action) {
        when (action) {
            is Navigator.Action.Refresh -> actionsSubject.accept(NewsContract.Action.Refresh())
        }
    }

    private fun hideFab() {
        (activity as NavigatorActivity).hideFab()
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(activity)
        news_newsList.adapter = adapter
        news_newsList.layoutManager = layoutManager
        news_newsList.addItemDecoration(DividerItemDecoration(
                news_newsList.context, layoutManager.orientation))
    }

    override fun onStateReceived(state: NewsContract.State) {
        when (state) {
            is NewsContract.State.Refreshed -> {
                navigator?.onStateReceived(Navigator.State.Refreshed())
            }
            is NewsContract.State.News -> {
                navigator?.onStateReceived(Navigator.State.Refreshed())
                displayNews(state.marketNews)
            }
            is NewsContract.State.Error -> {
                news_loadingFrame.setState(LoadingFrame.ERROR)
                navigator?.onStateReceived(Navigator.State.Refreshed())
                displayError()
            }
            is NewsContract.State.Link -> openLink(state.url)
        }
    }

    private fun displayNews(marketNews: List<MarketNews>) {
        adapter.marketNews = marketNews
        news_loadingFrame.setState(LoadingFrame.SUCCESS)
    }

    private fun displayError() {
        news_loadingFrame.setErrorText(R.string.error_generic)
        news_loadingFrame.setState(LoadingFrame.ERROR)
    }

    private fun openLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}