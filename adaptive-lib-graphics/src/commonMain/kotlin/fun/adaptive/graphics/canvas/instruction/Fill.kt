/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.graphics.canvas.render.GraphicsRenderData
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.utility.alsoIfInstance

data class Fill(
    val color: Color,
) : GraphicsInstruction {

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<GraphicsRenderData> {
            it.fill = this
        }
    }
}