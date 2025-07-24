package `fun`.adaptive.chart.normalization

import `fun`.adaptive.chart.model.ChartDataRange
import kotlin.time.Instant

class InstantDoubleNormalizer(
    range: ChartDataRange<Instant, Double>,
) : AbstractDoubleNormalizer<Instant, Double>(range) {

    override val Instant.asXDouble: Double
        get() = epochSeconds.toDouble()

    override val Double.asYDouble: Double
        get() = this

    override val Double.asXt: Instant
        get() = this.toLong().let(Instant::fromEpochSeconds)

    override val Double.asYt: Double
        get() = this

}