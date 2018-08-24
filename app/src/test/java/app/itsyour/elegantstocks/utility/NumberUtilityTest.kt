package app.itsyour.elegantstocks.utility

import org.junit.Test
import kotlin.test.assertEquals

class NumberUtilityTest {
    @Test
    fun shouldFormatToTwoDecimalsAndRoundUp() {
        assertEquals("50.33%", .503250000F.percentToScreen())
    }

    @Test
    fun shouldFormatToTwoDecimalsAndRoundDown() {
        assertEquals("50.32%", .503240000F.percentToScreen())
    }

    @Test
    fun shouldPreserveNegative() {
        assertEquals("-5.02%", (-.05023000F).percentToScreen())
    }

    @Test
    fun shouldRenderVerySmallNumber() {
        assertEquals("0.00%", (.0000000524F).percentToScreen())
    }

    @Test
    fun shouldDropNegativeSignIfValueIsZero() {
        assertEquals("0.00%", (-.0000000524F).percentToScreen())
    }

    @Test
    fun shouldRenderVeryLargeNumber() {
        assertEquals("12303.30%", 123.032999F.percentToScreen())
    }
}