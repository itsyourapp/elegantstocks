package app.itsyour.elegantstocks.feature.chart.candle

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class CandleChart
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0): CandleStickChart(context, attrs, defStyle) {

    fun initialize() {
        setTouchEnabled(true)
        isDragEnabled = true
        isScaleYEnabled = false
        isScaleXEnabled = true
        setBackgroundColor(Color.WHITE)
        description.isEnabled = false
        setMaxVisibleValueCount(20)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        axisRight.isEnabled = false
        legend.isEnabled = false
    }

    fun setData(yVals1: List<CandleEntry>) {
        val set1 = CandleDataSet(yVals1, "Data Set")
        set1.setDrawIcons(false)
        set1.axisDependency = YAxis.AxisDependency.LEFT
        set1.shadowColor = Color.DKGRAY
        set1.decreasingColor = Color.RED
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = Color.rgb(122, 242, 84)
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.BLUE
        set1.shadowWidth = 2f
        data = CandleData(set1)
        data.setValueTextSize(12f)
        zoom(5f, 1f, center.x, center.y)
        moveViewToX(xChartMax)

    }

    fun setHorizontalAsymptote(banner: String, position: Int) {
        val yMax = data.dataSets[0].yMax
        val yMin = data.dataSets[0].yMin

        if (position > yMax) {
            axisLeft.axisMaximum = position + (position - yMin) * .10f
            axisLeft.axisMinimum = yMin
        } else if (position < yMin) {
            axisLeft.axisMinimum = position - (yMax - position) * .10f
            axisLeft.axisMaximum = yMax
        } else {
            axisLeft.axisMaximum = yMax
            axisLeft.axisMinimum = yMin
        }

        val ll = LimitLine(position.toFloat())
        ll.lineColor = Color.BLACK
        ll.lineWidth = 1f
        ll.label = banner
        axisLeft.removeAllLimitLines()
        axisLeft.addLimitLine(ll)

        invalidate()
    }
}