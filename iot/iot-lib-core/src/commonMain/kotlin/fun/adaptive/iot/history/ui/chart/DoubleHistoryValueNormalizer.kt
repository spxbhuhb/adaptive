package `fun`.adaptive.iot.history.ui.chart

import `fun`.adaptive.chart.model.ChartDataRange
import `fun`.adaptive.chart.normalization.AbstractDoubleNormalizer
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import kotlinx.datetime.Instant

class DoubleHistoryValueNormalizer(
    range: ChartDataRange<Instant, AioDoubleHistoryRecord>,
) : AbstractDoubleNormalizer<Instant, AioDoubleHistoryRecord>(range) {

    override val Instant.asXDouble: Double
        get() = epochSeconds.toDouble()

    override val AioDoubleHistoryRecord.asYDouble: Double
        get() = this.value

    override val Double.asXt: Instant
        get() = this.toLong().let(Instant::fromEpochSeconds)

    override val Double.asYt: AioDoubleHistoryRecord
        get() = AioDoubleHistoryRecord(timestamp = Instant.DISTANT_PAST, rawValue = this, value = this, flags = 0)

}