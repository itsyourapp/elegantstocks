package app.itsyour.elegantstocks.feature.navigator.news

import app.itsyour.elegantstocks.feature.base.BasePresenter
import app.itsyour.elegantstocks.feature.navigator.news.model.MarketNews
import app.itsyour.elegantstocks.feature.navigator.news.model.NewsInteractor
import io.reactivex.rxkotlin.plusAssign
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class NewsPresenter
        @Inject constructor(
                private val interactor: NewsInteractor)
                    : BasePresenter<NewsContract.View>(), NewsContract.Presenter {

    override fun onViewTaken() {
        listenForViewActions()
        fetchNews()
    }

    private fun listenForViewActions() {
        subscriptions += view.actions.subscribe(::onActionReceived)
    }

    private fun onActionReceived(action: NewsContract.Action) {
        when (action) {
            is NewsContract.Action.Refresh -> refresh()
            is NewsContract.Action.Select -> onNewsSelected(action.marketNews)
        }
    }

    private fun onNewsSelected(news: MarketNews) {
        view.onStateReceived(NewsContract.State.Link(news.url))
    }

    private fun fetchNews() {
        subscriptions += interactor.fetchNews()
                .subscribe(::onNewsReceived, ::onNewsErrored)
    }

    private fun onNewsReceived(marketNews:  List<MarketNews>) {
        view.onStateReceived(NewsContract.State.News(marketNews))
    }

    private fun onNewsErrored(error: Throwable) {
        // TODO: Handle multiple error states
        @Suppress("USELESS_IS_CHECK")
        when (error) {
            is HttpException,
            is UnknownHostException,
            is Any -> view.onStateReceived(NewsContract.State.Error())
        }
    }

    private fun refresh() {
        fetchNews()
    }
}