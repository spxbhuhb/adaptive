/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.instruction

import hu.simplexion.adaptive.grapics.svg.parse.SvgInstruction
import hu.simplexion.adaptive.grapics.svg.render.SvgRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

interface SvgTransform : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRenderData> {
            val transforms = it.transform
            if (transforms == null) {
                it.transform = mutableListOf(this)
            } else {
                transforms += this
            }
        }
    }
}

data class Matrix(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double,
    val e: Double,
    val f: Double
) : SvgTransform

data class Translate(
    val tx: Double,
    val ty: Double,
) : SvgTransform

data class Scale(
    val sx: Double,
    val sy: Double
) : SvgTransform

data class Rotate(
    val rotateAngle : Double,
    val cx : Double,
    val cy : Double
) : SvgTransform

data class SkewX(
    val skewAngle : Double
) : SvgTransform

data class SkewY(
    val skewAngle : Double
) : SvgTransform

