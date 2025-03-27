package `fun`.adaptive.chart.normalization

import `fun`.adaptive.chart.model.ChartDataRange

abstract class AbstractDoubleNormalizer<XT,YT>(
    range: ChartDataRange<XT, YT>,
) : ChartNormalizer<XT, YT>() {

    val xStart = range.xStart.asXDouble
    val yStart = range.yStart.asYDouble
    val xRange = range.xEnd.asXDouble - xStart
    val yRange = range.yEnd.asYDouble - yStart

    override fun normalizeX(x: XT): Double =
        (x.asXDouble - xStart) / xRange

    override fun normalizeY(y: YT): Double =
        (y.asYDouble - yStart) / yRange

    override fun denormalizeX(x: Double): XT =
        (x * xRange + xStart).asXt

    override fun denormalizeY(y: Double): YT =
        (y * yRange + yStart).asYt

    abstract val XT.asXDouble : Double

    abstract val YT.asYDouble : Double

    abstract val Double.asXt : XT

    abstract val Double.asYt : YT
}