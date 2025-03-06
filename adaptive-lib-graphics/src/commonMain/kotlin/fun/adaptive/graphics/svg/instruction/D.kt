/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.canvas.path.PathCommand
import `fun`.adaptive.graphics.svg.parse.SvgInstruction
import `fun`.adaptive.graphics.svg.render.SvgPathRenderData
import `fun`.adaptive.utility.alsoIfInstance

data class D(
    val commands: List<PathCommand>
) : SvgInstruction {
    override fun applyTo(subject: Any) {
        subject.alsoIfInstance<SvgPathRenderData> {
            it.commands = commands
        }
    }
}