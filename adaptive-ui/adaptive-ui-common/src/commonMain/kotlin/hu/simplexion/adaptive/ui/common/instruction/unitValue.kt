/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.ui.common.AdaptiveUIAdapter

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

    fun toPx(adapter: AdaptiveUIAdapter<*, *>): Float =
        if (this === ZERO) 0f else adapter.toPx(this)

    override fun toRawValue(adapter: AdaptiveUIAdapter<*, *>): Float =
        if (this === ZERO) 0f else adapter.toPx(this)

    override fun toString(): String {
        return "${value}dp"
    }

    companion object {
        val ZERO = DPixel(0f)
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

    override fun toRawValue(adapter: AdaptiveUIAdapter<*, *>): Float = value

    override fun toString(): String {
        return "${value}fr"
    }
}
