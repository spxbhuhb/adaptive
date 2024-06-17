/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

// -----------------------------------------------------------
// Device independent pixel
// -----------------------------------------------------------

val Int.dp: DPixel
    get() = DPixel(this.toDouble())

val Double.dp: DPixel
    get() = DPixel(this)

/**
 * Convert the device-independent position into device-dependent pixel value.
 *
 * - values between 0.000001 and 0.000001 are converted to 0.0
 *
 * https://stackoverflow.com/questions/21260776/how-to-compare-double-numbers
 */
fun DPixel.toPx(adapter: AbstractCommonAdapter<*, *>): Double =
    when {
        (- 0.000001 < this.value && this.value < 0.000001) -> 0.0
        else -> adapter.toPx(this)
    }

/**
 * Convert the device-independent position into device-dependent pixel value.
 *
 * - null stays null
 * - values between 0.000001 and 0.000001 are converted to 0.0
 *
 * https://stackoverflow.com/questions/21260776/how-to-compare-double-numbers
 */
fun DPixel?.toPx(adapter: AbstractCommonAdapter<*, *>): Double? =
    when {
        this == null -> null
        (- 0.000001 < this.value && this.value < 0.000001) -> 0.0
        else -> adapter.toPx(this)
    }

/**
 * Convert the device-independent position into device-dependent pixel value.
 *
 * - null is converted to 0.0
 * - values between 0.000001 and 0.000001 are converted to 0.0
 *
 * https://stackoverflow.com/questions/21260776/how-to-compare-double-numbers
 */
fun DPixel?.toPxOrZero(adapter: AbstractCommonAdapter<*, *>): Double =
    if (this == null || (- 0.000001 < this.value && this.value < 0.000001)) 0.0 else adapter.toPx(this)

data class DPixel(
    override val value: Double
) : Track {

    override val isFix
        get() = true

    override fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Double =
        this.toPxOrZero(adapter)

    override fun toString(): String {
        return if (value.isNaN()) "NaP" else "${value}dp"
    }

    companion object {
        val ZERO = DPixel(0.0)
    }
}

// -----------------------------------------------------------
// Scaled pixel (used for text sizes)
// -----------------------------------------------------------

val Int.sp: SPixel
    get() = SPixel(this.toDouble())

val Double.sp: SPixel
    get() = SPixel(this)

data class SPixel(
    val value: Double
) {

    override fun toString(): String {
        return "${value}sp"
    }
}

// -----------------------------------------------------------
// Fraction
// -----------------------------------------------------------

val Int.fr: Fraction
    get() = Fraction(this.toDouble())

val Double.fr: Fraction
    get() = Fraction(this)

data class Fraction(
    override val value: Double
) : Track {

    override val isFix
        get() = false

    override fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Double = value

    override fun toString(): String {
        return "${value}fr"
    }
}

// -----------------------------------------------------------
// Surrounding
// -----------------------------------------------------------

interface Surrounding{
    val top: DPixel?
    val right: DPixel?
    val bottom: DPixel?
    val left: DPixel?
}