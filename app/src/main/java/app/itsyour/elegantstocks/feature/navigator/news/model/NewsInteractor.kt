package app.itsyour.elegantstocks.feature.navigator.news.model

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class NewsInteractor
    @Inject constructor(private val api: NewsApi) {
    
    companion object {
        private const val NEWS_FOR_MARKET = "market"
    }

    fun fetchNews(): Single<List<MarketNews>> {
        return api.fetchNews(NEWS_FOR_MARKET)
                .observeOn(AndroidSchedulers.mainThread())
    }
}