package `fun`.adaptive.iot.history.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.chart.model.AbstractChartDataPoint
import kotlinx.datetime.Instant

@Adat
class AioDoubleHistoryRecord(
    val timestamp: Instant,
    val value: Double,
    val flags: Int
) : AbstractChartDataPoint<Instant, AioDoubleHistoryRecord>() {

    override val x: Instant
        get() = timestamp

    override val y: AioDoubleHistoryRecord
        get() = this
}