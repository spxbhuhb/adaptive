/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.instruction

import hu.simplexion.adaptive.grapics.svg.parse.SvgInstruction
import hu.simplexion.adaptive.grapics.svg.render.SvgPathRenderData
import hu.simplexion.adaptive.grapics.svg.render.SvgRenderData
import hu.simplexion.adaptive.grapics.svg.render.SvgRootRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

class ViewBox(
    val minX : Double,
    val minY : Double,
    val width : Double,
    val height : Double
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.viewBox = this
        }
    }
}

class Height(
    val height : String
) : SvgInstruction {
    // TODO string to pixel width conversion with whatever exotic units, not sure if I want this
}

class Width(
    val width : String
) : SvgInstruction {
    // TODO string to pixel width conversion with whatever exotic units, not sure if I want this
}

class Fill(
    val fill : String
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRenderData> {
            it.fill = this
        }
    }
}

class D(
    val commands : List<SvgPathCommand>
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgPathRenderData> {
            it.commands = commands
        }
    }
}