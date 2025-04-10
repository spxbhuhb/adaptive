package `fun`.adaptive.chart.normalization

import `fun`.adaptive.chart.model.ChartDataRange

class DoubleDoubleNormalizer(
    range: ChartDataRange<Double, Double>,
) : ChartNormalizer<Double, Double>() {

    val xStart = range.xStart
    val yStart = range.yStart
    val xRange = range.xEnd - xStart
    val yRange = range.yEnd - yStart

    override fun normalizeX(x: Double): Double =
        (x - xStart) / xRange

    override fun normalizeY(y: Double): Double =
        (y - yStart) / yRange

    override fun denormalizeX(x: Double): Double =
        x * xRange + xStart

    override fun denormalizeY(y: Double): Double =
        y * yRange + yStart

}