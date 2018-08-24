package app.itsyour.elegantstocks.feature.navigator.quotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.math.BigDecimal

/**
 * Represents a real-time quote for a particular symbol.
 */
@Entity
data class Quote(
        @PrimaryKey
        @Expose val symbol: String,
        @Expose val companyName: String,
        @Expose val primaryExchange: String,
        @Expose val sector: String,
        @Expose val calculationPrice: String,
        @Expose val open: BigDecimal,
        @Expose val openTime: Long,
        @Expose val close: BigDecimal,
        @Expose val closeTime: Long,
        @Expose val high: BigDecimal?,
        @Expose val low: BigDecimal?,
        @Expose val latestPrice: BigDecimal,
        @Expose val latestSource: String,
        @Expose val latestTime: String,
        @Expose val latestUpdate: Long,
        @Expose val latestVolume: Long,
        @Expose val delayedPrice: BigDecimal,
        @Expose val delayedPriceTime: Long,
        @Expose val previousClose: BigDecimal,
        @Expose val change: Float,
        @Expose val changePercent: Float,
        @Expose val marketCap: Long,
        @Expose val peRatio: Float,
        @Expose val week52High: BigDecimal,
        @Expose val week52Low: BigDecimal,
        @Expose val ytdChange: Float)