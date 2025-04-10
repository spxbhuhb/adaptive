/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.graphics.svg.render.SvgRenderData
import `fun`.adaptive.utility.alsoIfInstance

interface CanvasTransformInstruction : CanvasInstruction {

    override fun applyTo(subject: Any) {

        subject.alsoIfInstance<CanvasRenderData> {
            val transforms = it.transforms
            if (transforms == null) {
                it.transforms = mutableListOf(this)
            } else {
                transforms += this
            }
        }

    }

}