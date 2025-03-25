package `fun`.adaptive.chart.normalization

import `fun`.adaptive.chart.model.ChartDataRange
import kotlinx.datetime.Instant

class InstantDoubleNormalizer(
    range: ChartDataRange<Instant, Double>,
) : AbstractDoubleNormalizer<Instant>(range) {

    override val Instant.asDouble: Double
        get() = epochSeconds.toDouble()

    override val Double.asXt: Instant
        get() = this.toLong().let(Instant::fromEpochSeconds)

}