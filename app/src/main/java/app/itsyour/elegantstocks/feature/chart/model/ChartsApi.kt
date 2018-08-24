package app.itsyour.elegantstocks.feature.chart.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ChartsApi {
    @GET("/1.0/stock/{symbol}/chart/{range}")
    fun fetchChart(@Path("symbol") symbol: String,
                   @Path("range") range: String): Single<List<ChartEntry>>
}