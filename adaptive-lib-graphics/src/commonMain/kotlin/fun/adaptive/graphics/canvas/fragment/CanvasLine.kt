/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas

@AdaptiveActual(canvas)
open class CanvasLine(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    val x1: Double
        by stateVariable()

    val y1: Double
        by stateVariable()

    val x2: Double
        by stateVariable()

    val y2: Double
        by stateVariable()

    override fun draw() {
        trace("draw")

        canvas.save(id)

        canvas.setStroke(renderData.stroke.color)

        canvas.line(x1, y1, x2, y2)

        canvas.restore(id)
    }

}