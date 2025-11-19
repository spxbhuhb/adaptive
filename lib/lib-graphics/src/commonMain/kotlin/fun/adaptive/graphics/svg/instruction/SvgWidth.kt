/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.canvas.instruction.CanvasInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.ui.render.model.LayoutRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class SvgWidth(
    val width: Double
) : CanvasInstruction {

    // TODO exotic SVG units
    constructor(width: String) : this(width.removeSuffix("px").toDouble())

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.width = this.width
            // FIXME svg sizing hack (here to make pre-rendered SVGs work)
            (it.layout ?: LayoutRenderData(it.adapter).also { lrd -> it.layout = lrd }).instructedWidth = this.width
        }
    }
}