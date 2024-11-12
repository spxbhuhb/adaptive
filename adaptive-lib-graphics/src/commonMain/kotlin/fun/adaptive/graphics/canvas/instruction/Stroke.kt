/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.graphics.canvas.render.GraphicsRenderData
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.utility.alsoIfInstance

data class Stroke(
    val color: Color
) : GraphicsInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<GraphicsRenderData> {
            it.stroke = this
        }
    }
}