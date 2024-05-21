/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

class Pixel(
    override val value : Float
) : Track {

    override val isFix
        get() = true

}

class Fraction(
    override val value : Float
) : Track {

    override val isFix
        get() = false

}

val Int.px : Pixel
    get() = Pixel(this.toFloat())

val Float.px : Pixel
    get() = Pixel(this)

val Double.px : Pixel
    get() = Pixel(this.toFloat())

val Int.fr : Fraction
    get() = Fraction(this.toFloat())

val Float.fr : Fraction
    get() = Fraction(this)

val Double.fr : Fraction
    get() = Fraction(this.toFloat())