/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.graphics.canvas.instruction.CanvasInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class SvgWidth(
    val width: Double
) : CanvasInstruction {

    // TODO exotic SVG units
    constructor(width: String) : this(width.removeSuffix("px").toDouble())

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.width = this.width
        }
    }
}