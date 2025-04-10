package `fun`.adaptive.chart.normalization


class NullNormalizer<XT, YT> : ChartNormalizer<XT, YT>() {

    override fun normalizeX(x: XT): Double = Double.NaN

    override fun normalizeY(y: YT): Double = Double.NaN

    override fun denormalizeX(x: Double): XT? = null

    override fun denormalizeY(y: Double): YT? = null

}