package `fun`.adaptive.resource.language

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.localizedString() =
    toLocalDateTime(TimeZone.currentSystemDefault()).toString().replace("T", " ").substringBefore('.')