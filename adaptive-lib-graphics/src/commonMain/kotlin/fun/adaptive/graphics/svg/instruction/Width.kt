/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.svg.parse.SvgInstruction
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class Width(
    val width: String
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.width = width.removeSuffix("px").toDouble() // TODO exotic SVG units
        }
    }
}