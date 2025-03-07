/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.utility.alsoIfInstance

@Adat
class Stroke(
    val color: Color
) : CanvasInstruction {

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<CanvasRenderData> {
            it.stroke = this
        }
    }
}