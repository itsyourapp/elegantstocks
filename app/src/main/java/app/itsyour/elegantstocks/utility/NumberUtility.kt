package app.itsyour.elegantstocks.utility

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

fun Float.percentToScreen(): String {
    val render = "${String.format(Locale.US, "%.${2}f", this * 100)}%"
    return if (render == "-0.00%") render.drop(1) else render
}

fun BigDecimal.currencyToScreen() = "$"+ DecimalFormat("0.00").format(this)

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start
