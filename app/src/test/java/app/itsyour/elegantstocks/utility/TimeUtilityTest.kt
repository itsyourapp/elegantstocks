package app.itsyour.elegantstocks.utility

import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class TimeUtilityTest {
    @Before
    fun setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun shouldFormatWithDefaultTimezone() {
        val epoch = 1294708260L * 1000  // 2011-01-11 01:11 UTC
        assertEquals("1:11AM on Jan 11 2011 (UTC)", epoch.epochToScreen())
    }

    @Test
    fun shouldFormatWithLocalTimezone() {
        val epoch = 1294708260L * 1000  // 2011-01-11 01:11 UTC
        TimeZone.setDefault(TimeZone.getTimeZone("EST"))
        assertEquals("8:11PM on Jan 10 2011 (-05:00)", epoch.epochToScreen())
    }
}