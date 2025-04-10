package `fun`.adaptive.iot.history.ui.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.localDate
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Adat
data class HistoryContentConfig(
    val start : LocalDate = localDate(),
    val end : LocalDate = localDate(),
    val interval : Duration = 15.minutes,
    val nameMethod : NameMethod = NameMethod.ParentAndUnit,
    val selectedDuration : NamedDurationType = NamedDurationType.Today
)