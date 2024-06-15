/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

// -----------------------------------------------------------
// Device independent pixel
// -----------------------------------------------------------

val Int.dp: DPixel
    get() = DPixel(this.toFloat())

val Float.dp: DPixel
    get() = DPixel(this)

val Double.dp: DPixel
    get() = DPixel(this.toFloat())

data class DPixel(
    override val value: Float
) : Track {

    override val isFix
        get() = true

    infix fun or(other: DPixel): DPixel =
        if (this === NaP) other else this

    /**
     * Convert the device-independent point into device-dependent pixel value.
     * [NaP] is converted to [Float.NaN].
     */
    fun toPx(adapter: AbstractCommonAdapter<*, *>): Float =
        when {
            this == NaP -> Float.NaN
            this === ZERO -> 0f
            else -> adapter.toPx(this)
        }

    /**
     * Convert the device-independent point into device-dependent pixel value.
     * [NaP] is converted to 0f. If you want to keep the information that the
     * value is invalid use [toPx].
     */
    fun toPxOrZero(adapter: AbstractCommonAdapter<*, *>): Float =
        if (this === ZERO || this === NaP) 0f else adapter.toPx(this)

    override fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Float =
        this.toPx(adapter)

    override fun toString(): String {
        return if (value.isNaN()) "NaP" else "${value}dp"
    }

    companion object {
        val ZERO = DPixel(0f)

        /**
         * Means that this value is unspecified.
         */
        val NaP = DPixel(Float.NaN)
    }
}

// -----------------------------------------------------------
// Scaled pixel (used for text sizes)
// -----------------------------------------------------------

val Int.sp: SPixel
    get() = SPixel(this.toFloat())

val Float.sp: SPixel
    get() = SPixel(this)

val Double.sp: SPixel
    get() = SPixel(this.toFloat())

data class SPixel(
    val value: Float
) {

    override fun toString(): String {
        return "${value}sp"
    }
}

// -----------------------------------------------------------
// Fraction
// -----------------------------------------------------------

val Int.fr: Fraction
    get() = Fraction(this.toFloat())

val Float.fr: Fraction
    get() = Fraction(this)

val Double.fr: Fraction
    get() = Fraction(this.toFloat())

data class Fraction(
    override val value: Float
) : Track {

    override val isFix
        get() = false

    override fun toRawValue(adapter: AbstractCommonAdapter<*, *>): Float = value

    override fun toString(): String {
        return "${value}fr"
    }
}
