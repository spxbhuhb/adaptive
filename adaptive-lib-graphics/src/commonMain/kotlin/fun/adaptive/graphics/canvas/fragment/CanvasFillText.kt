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
open class CanvasFillText(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, 3, stateSize()) {

    val x: Double
        by stateVariable()

    val y: Double
        by stateVariable()

    val text: String
        by stateVariable()

    val fakeInstructions: AdaptiveInstructionGroup
        by stateVariable()

    override fun draw() {
        trace("draw")

        canvas.save(id)

        canvas.setFill(renderData.fill.color)

        canvas.fillText(x, y, text)

        canvas.restore(id)
    }

}