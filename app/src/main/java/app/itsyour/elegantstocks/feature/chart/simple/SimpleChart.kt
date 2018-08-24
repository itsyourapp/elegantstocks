package app.itsyour.elegantstocks.feature.chart.simple

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import app.itsyour.elegantstocks.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.absoluteValue

class SimpleChart
    @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LineChart(context, attrs, defStyleAttr) {

    override fun init() {
        super.init()
        description.isEnabled = false
        setDrawGridBackground(false)
        setTouchEnabled(false)
        isDragEnabled = true
        setScaleEnabled(true)
        setPinchZoom(false)
        legend.isEnabled = false
        axisLeft.isEnabled = false
        axisLeft.spaceTop = 8f
        axisLeft.spaceBottom = 8f
        axisRight.isEnabled = false
        xAxis.isEnabled = false
        animateX(200)
    }

    fun setChartEntries(chartEntries: List<Entry>) {
        val set = LineDataSet(chartEntries, "Dataset")
        set.lineWidth = 1.75f
        set.circleRadius = 5f
        set.circleHoleRadius = 2.5f
        set.color = Color.WHITE
        set.setCircleColor(Color.WHITE)
        set.highLightColor = Color.WHITE
        set.setDrawValues(false)
        set.setCircleColorHole(trendColor(chartEntries))
        data = LineData(set)
        setBackgroundColor(trendColor(chartEntries))
        invalidate()
    }

    private fun trendColor(entries: List<Entry>): Int {
        val trend = (entries.last().y - entries.first().y)
        if (trend.absoluteValue <= .10f) {
            return context.getColor(R.color.chartTrendNeutral)
        }
        return if (trend < 0f)
            context.getColor(R.color.chartTrendDown)
        else
            context.getColor(R.color.chartTrendUp)
    }
}