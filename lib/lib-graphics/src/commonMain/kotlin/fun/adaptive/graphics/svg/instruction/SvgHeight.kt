/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.canvas.instruction.CanvasInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.ui.render.model.LayoutRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class SvgHeight(
    val height: Double
) : CanvasInstruction {

    // TODO exotic SVG units
    constructor(height: String) : this(height.removeSuffix("px").toDouble())

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.height = this.height
            // FIXME svg sizing hack (here to make pre-rendered SVGs work)
            (it.layout ?: LayoutRenderData(it.adapter).also { lrd -> it.layout = lrd }).instructedHeight = this.height
        }
    }
}