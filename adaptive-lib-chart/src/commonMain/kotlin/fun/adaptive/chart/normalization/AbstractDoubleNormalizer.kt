package `fun`.adaptive.chart.normalization

import `fun`.adaptive.chart.model.ChartDataRange

abstract class AbstractDoubleNormalizer<XT>(
    range: ChartDataRange<XT, Double>,
) : ChartNormalizer<XT, Double>() {

    val xStart = range.xStart.asDouble
    val yStart = range.yStart
    val xRange = range.xEnd.asDouble - xStart
    val yRange = range.yEnd - yStart

    override fun normalizeX(x: XT): Double =
        (x.asDouble - xStart) / xRange

    override fun normalizeY(y: Double): Double =
        (y - yStart) / yRange

    override fun denormalizeX(x: Double): XT =
        (x * xRange + xStart).asXt

    override fun denormalizeY(y: Double): Double =
        y * yRange + yStart

    abstract val XT.asDouble : Double

    abstract val Double.asXt : XT
}