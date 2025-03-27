package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.CartesianPoint
import kotlinx.datetime.Instant

@Adat
class AioDoubleHistoryRecord(
    val timestamp: Instant,
    val value: Double,
    val flags: Int
) : CartesianPoint<Instant, AioDoubleHistoryRecord>() {

    override val x: Instant
        get() = timestamp

    override val y: AioDoubleHistoryRecord
        get() = this
}