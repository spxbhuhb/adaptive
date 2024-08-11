package `fun`.adaptive.adat


import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.instant
import `fun`.adaptive.utility.isBetween
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Adat
class TestAdat(
    val instant: Instant,
    val localDateTime: LocalDateTime,
    val localDate: LocalDate,
    val localTime: LocalTime,
    val duration: Duration
)

fun box(): String {
    val before = instant()
    val t1 = TestAdat()
    val after = instant()

    if (! t1.instant.isBetween(before, after)) return "Fail: instant $before ${t1.instant} $after"
    if (! t1.localDateTime.isBetween(before, after)) return "Fail: localDateTime"
    if (! t1.localDate.isBetween(before.minus(1.days), after)) return "Fail: localDate $before ${t1.localDate} $after"
    if (! t1.localTime.isBetween(before, after)) return "Fail: localTime"
    if (t1.duration != Duration.ZERO) return "Fail: duration"

    return "OK"
}