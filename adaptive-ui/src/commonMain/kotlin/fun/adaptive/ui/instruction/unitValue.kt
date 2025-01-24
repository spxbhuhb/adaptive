/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.layout.GridRepeat
import `fun`.adaptive.ui.instruction.layout.GridTrack

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
fun DPixel.toPx(adapter: AbstractAuiAdapter<*, *>): Double =
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
fun DPixel?.toPx(adapter: AbstractAuiAdapter<*, *>): Double? =
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
fun DPixel?.toPxOrZero(adapter: AbstractAuiAdapter<*, *>): Double =
    if (this == null || (- 0.000001 < this.value && this.value < 0.000001)) 0.0 else adapter.toPx(this)

@Adat
class DPixel(
    override val value: Double
) : GridTrack {

    override val isFix
        get() = true

    override val isExtend: Boolean
        get() = false

    override fun toRawValue(adapter: AbstractAuiAdapter<*, *>): Double =
        this.toPxOrZero(adapter)

    override fun toString(): String {
        return if (value.isNaN()) "NaP" else "${value}dp"
    }

    operator fun minus(value: Double) = DPixel(this.value - value)

    operator fun plus(value: Double) = DPixel(this.value + value)

    operator fun minus(value: DPixel) = DPixel(this.value - value.value)

    operator fun plus(value: DPixel) = DPixel(this.value + value.value)

    operator fun times(value: Int) = DPixel(this.value * value)

    operator fun times(value: Double) = DPixel(this.value * value)

    operator fun div(value: Double) = DPixel(this.value / value)

    operator fun div(value: DPixel) = DPixel(this.value / value.value)

    infix fun repeat(count: Int): GridRepeat = GridRepeat(count, this)

    companion object {
        val ZERO = DPixel(0.0)
        val NaP = DPixel(Double.NaN)
    }
}

// -----------------------------------------------------------
// Scaled pixel (used for text sizes)
// -----------------------------------------------------------

val Int.sp: SPixel
    get() = SPixel(this.toDouble())

val Double.sp: SPixel
    get() = SPixel(this)

@Adat
class SPixel(
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

@Adat
class Fraction(
    override val value: Double
) : GridTrack {

    override val isFraction: Boolean
        get() = true

    override val isExtend: Boolean
        get() = false

    infix fun repeat(count: Int): GridRepeat = GridRepeat(count, this)

    override fun toRawValue(adapter: AbstractAuiAdapter<*, *>): Double = value

    override fun toString(): String {
        return "${value}fr"
    }
}

// -----------------------------------------------------------
// Surrounding
// -----------------------------------------------------------

interface Surrounding {
    val top: DPixel?
    val right: DPixel?
    val bottom: DPixel?
    val left: DPixel?
}