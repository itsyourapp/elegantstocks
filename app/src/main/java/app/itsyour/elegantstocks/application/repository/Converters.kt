package app.itsyour.elegantstocks.application.repository

import androidx.room.TypeConverter
import app.itsyour.elegantstocks.feature.chart.model.ChartEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal


object Converters {
    @TypeConverter @JvmStatic
    fun fromLong(value: Long?): BigDecimal? =
         if (value == null) null else BigDecimal(value).divide(BigDecimal(100))

    @TypeConverter @JvmStatic
    fun toLong(bigDecimal: BigDecimal?): Long? =
        bigDecimal?.multiply(BigDecimal(100))?.toLong()

    @TypeConverter @JvmStatic
    fun fromChartEntryList(entries: List<ChartEntry>?): String? {
        if (entries == null) return null
        val type = object : TypeToken<List<ChartEntry>>() {}.type
        return Gson().toJson(entries, type)
    }

    @TypeConverter @JvmStatic
    fun toChartEntryList(entriesString: String?): List<ChartEntry>? {
        if (entriesString == null) return null
        val type = object : TypeToken<List<ChartEntry>>() {}.type
        return Gson().fromJson<List<ChartEntry>>(entriesString, type)
    }
}
