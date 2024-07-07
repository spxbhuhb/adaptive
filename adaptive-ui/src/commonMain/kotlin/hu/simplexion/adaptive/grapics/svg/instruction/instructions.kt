/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.instruction

import hu.simplexion.adaptive.grapics.svg.parse.SvgInstruction
import hu.simplexion.adaptive.grapics.svg.render.SvgPathRenderData
import hu.simplexion.adaptive.grapics.svg.render.SvgRenderData
import hu.simplexion.adaptive.grapics.svg.render.SvgRootRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class ViewBox(
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

data class Height(
    val height : String
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.height = height.removeSuffix("px").toDouble() // TODO exotic SVG units
        }
    }
}

data class Width(
    val width : String
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRootRenderData> {
            it.width = width.removeSuffix("px").toDouble() // TODO exotic SVG units
        }
    }
}

data class Fill(
    val fill : String
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRenderData> {
            it.fill = this
        }
    }
}

data class D(
    val commands : List<SvgPathCommand>
) : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgPathRenderData> {
            it.commands = commands
        }
    }
}