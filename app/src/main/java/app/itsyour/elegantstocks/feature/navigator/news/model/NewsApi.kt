package app.itsyour.elegantstocks.feature.navigator.news.model

import app.itsyour.elegantstocks.application.network.CacheInterceptor
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Fetches real-time marketNews for the market.
 */
interface NewsApi {
    private val defaultItemsToFetch get() = 10

    /**
     * Unfortunately the API appears to be broken and does not support a range > 10.
     * https://github.com/iexg/IEX-API/issues/361
     */
    @GET("/1.0/stock/{symbol}/news/last/{range}")
    @Headers(CacheInterceptor.NO_CACHE_HEADER)
    fun fetchNews(@Path("symbol") symbol: String, @Path("range") range: Int = defaultItemsToFetch): Single<List<MarketNews>>
}