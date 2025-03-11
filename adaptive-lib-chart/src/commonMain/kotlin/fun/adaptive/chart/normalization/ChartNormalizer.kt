package `fun`.adaptive.chart.normalization

abstract class ChartNormalizer<XT, YT> {

    abstract fun normalizeX(x: XT): Double

    abstract fun normalizeY(y: YT): Double

    abstract fun denormalizeX(x: Double): XT?

    abstract fun denormalizeY(y: Double): YT?

}