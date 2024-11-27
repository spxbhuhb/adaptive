/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.instruction.SvgFill
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.utility.checkIfInstance
import kotlin.math.PI

@AdaptiveActual(canvas)
open class CanvasLine(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, 4, 5) {

    val x1: Double
        get() = state[0].checkIfInstance()

    val y1: Double
        get() = state[1].checkIfInstance()

    val x2: Double
        get() = state[2].checkIfInstance()

    val y2: Double
        get() = state[3]?.checkIfInstance() ?: 0.0

    override fun draw() {
        trace("draw")

        canvas.save(id)

        canvas.setStroke(renderData.stroke.color)

        canvas.line(x1, y1, x2, y2)

        canvas.restore(id)
    }

}