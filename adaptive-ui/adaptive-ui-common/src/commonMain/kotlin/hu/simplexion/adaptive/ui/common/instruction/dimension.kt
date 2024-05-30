/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

data class DIPixel(
    override val value : Float
) : Track {

    override val isFix
        get() = true

    override fun toString(): String {
        return "${value}dp"
    }
}

data class Fraction(
    override val value : Float
) : Track {

    override val isFix
        get() = false

    override fun toString(): String {
        return "${value}fr"
    }
}

val Int.dp : DIPixel
    get() = DIPixel(this.toFloat())

val Float.px : DIPixel
    get() = DIPixel(this)

val Double.px : DIPixel
    get() = DIPixel(this.toFloat())

val Int.fr : Fraction
    get() = Fraction(this.toFloat())

val Float.fr : Fraction
    get() = Fraction(this)

val Double.fr : Fraction
    get() = Fraction(this.toFloat())