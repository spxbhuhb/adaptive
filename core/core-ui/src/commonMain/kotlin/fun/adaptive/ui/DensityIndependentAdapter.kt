package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel

/**
 * Defines conversion functions between density independent and density dependent values.
 */
abstract class DensityIndependentAdapter : AdaptiveAdapter {

    abstract fun toPx(dPixel: DPixel): Double

    abstract fun toDp(value: Double): DPixel

    abstract fun toPx(sPixel: SPixel): Double

}