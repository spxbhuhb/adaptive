/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.render

import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.canvas.instruction.Stroke
import `fun`.adaptive.graphics.canvas.instruction.CanvasTransformInstruction
import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.render.model.AuiRenderData

open class CanvasRenderData(
    adapter : DensityIndependentAdapter
) : AuiRenderData(adapter) {

    var fill: Fill? = null
    var stroke: Stroke? = null
    var transforms: MutableList<CanvasTransformInstruction>? = null

    override fun toString(): String {
        return "CanvasRenderData(fill=${fill?.color ?: "null"}, stroke=${stroke?.color ?: "null"}, transform=${transforms?.joinToString()})"
    }
}