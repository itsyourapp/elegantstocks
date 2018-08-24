package app.itsyour.elegantstocks.feature.symbol.model

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Fetches an array of symbols supported for trading.
 * The list is updated frequently by the service.
 */
interface SymbolApi {
    @get:GET("/1.0/ref-data/symbols")
    val symbols: Single<List<Symbol>>
}