/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.utility.checkIfInstance
import kotlin.math.PI

@AdaptiveActual(canvas)
open class CanvasFillText(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, 3, 4) {

    val x: Double
        get() = state[0].checkIfInstance()

    val y: Double
        get() = state[1].checkIfInstance()

    val text: String
        get() = state[2].checkIfInstance()

    override fun draw() {
        trace("draw")

        canvas.save(id)

        canvas.setFill(renderData.fill.color)

        canvas.fillText(x, y, text)

        canvas.restore(id)
    }

}