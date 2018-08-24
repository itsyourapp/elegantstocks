package app.itsyour.elegantstocks.utility

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun Long.epochToScreen(): String =
    Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("h:mma 'on' MMM d uuuu '('VV')'")
            .withZone(ZoneId.systemDefault()))

fun String.iso8601ToScreen(): String =
        OffsetDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .format(DateTimeFormatter.ofPattern("h:mma 'on' MMM d uuuu "))
