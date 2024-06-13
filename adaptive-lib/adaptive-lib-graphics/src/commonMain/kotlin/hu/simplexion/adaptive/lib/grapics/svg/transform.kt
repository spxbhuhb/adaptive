/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.grapics.svg

import hu.simplexion.adaptive.lib.grapics.svg.parse.SvgInstruction

interface SvgTransform : SvgInstruction

data class Matrix(
    val a: Float,
    val b: Float,
    val c: Float,
    val d: Float,
    val e: Float,
    val f: Float
) : SvgTransform

data class Translate(
    val tx: Float,
    val ty: Float,
) : SvgTransform

data class Scale(
    val sx: Float,
    val sy: Float
) : SvgTransform

data class Rotate(
    val rotateAngle : Float,
    val cx : Float,
    val cy : Float
) : SvgTransform

data class SkewX(
    val skewAngle : Float
) : SvgTransform

data class SkewY(
    val skewAngle : Float
) : SvgTransform

