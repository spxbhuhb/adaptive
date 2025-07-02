package `fun`.adaptive.lib.util.datetime

import `fun`.adaptive.adat.Adat
import kotlinx.datetime.LocalTime

@Adat
class TimeRange(
    val start : LocalTime = LocalTime(8,0),
    val end : LocalTime = LocalTime(16,0)
)