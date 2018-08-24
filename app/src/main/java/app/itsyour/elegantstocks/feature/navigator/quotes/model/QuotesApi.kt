package app.itsyour.elegantstocks.feature.navigator.quotes.model

import app.itsyour.elegantstocks.application.network.CacheInterceptor
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Fetches real-time symbols for a particular symbol.
 */
interface QuotesApi {
    @GET("/1.0/stock/market/batch?types=quote")
    @Headers(CacheInterceptor.NO_CACHE_HEADER)
    fun fetchQuotes(@Query("symbols") symbols: String): Single<Map<String, QuotesResponse>>
}