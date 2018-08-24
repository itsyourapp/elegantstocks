package app.itsyour.elegantstocks.feature.chart.model

import androidx.room.Entity
import com.google.gson.annotations.Expose
import java.math.BigDecimal
import java.util.*

@Entity
data class ChartEntry(
        @Expose val date: Date,
        @Expose val open: BigDecimal,
        @Expose val high: BigDecimal,
        @Expose val low: BigDecimal,
        @Expose val close: BigDecimal,
        @Expose val volume: Long,
        @Expose val unadjustedVolume: Long,
        @Expose val change: Double,
        @Expose val changePercent: Double,
        @Expose val vwap: BigDecimal,
        @Expose val label: String,
        @Expose val changeOverTime: Double)