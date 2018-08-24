package app.itsyour.elegantstocks.feature.navigator.quotes.model

import com.google.gson.annotations.Expose

/**
 * The exact data response object from the serivce API that represents
 * a real-time quote for a particular symbol.
 */
data class QuotesResponse(
        @Expose var quote: Quote)


