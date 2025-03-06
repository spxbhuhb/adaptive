/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.canvas.instruction.CanvasInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class ViewBox(
    val minX: Double,
    val minY: Double,
    val width: Double,
    val height: Double
) : CanvasInstruction {
    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.viewBox = this
        }
    }
}