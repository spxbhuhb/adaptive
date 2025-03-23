package `fun`.adaptive.iot.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.localizedString() =
    this
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toString()
        .replace("T", " ")
        .substringBeforeLast('.')
