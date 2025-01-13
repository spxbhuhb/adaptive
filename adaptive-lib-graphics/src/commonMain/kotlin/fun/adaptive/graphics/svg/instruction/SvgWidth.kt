/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.svg.instruction.SvgHeight
import `fun`.adaptive.graphics.svg.parse.SvgInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class SvgWidth(
    val width: Double
) : SvgInstruction {

    // TODO exotic SVG units
    constructor(width: String) : this(width.removeSuffix("px").toDouble())

    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.width = this.width
        }
    }
}