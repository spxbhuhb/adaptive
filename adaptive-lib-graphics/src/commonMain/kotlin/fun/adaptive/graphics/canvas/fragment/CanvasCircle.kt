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
open class CanvasCircle(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, 6, 7) {

    val centerX: Double
        get() = state[0].checkIfInstance()

    val centerY: Double
        get() = state[1].checkIfInstance()

    val radius: Double
        get() = state[2].checkIfInstance()

    val startAngle: Double
        get() = state[3]?.checkIfInstance() ?: 0.0

    val endAngle: Double
        get() = state[4]?.checkIfInstance() ?: (2 * PI)

    val anticlockwise: Boolean
        get() = state[5]?.checkIfInstance() ?: false

    override fun draw() {
        trace("draw")

        canvas.save(id)

        canvas.setFill(color(0xff0000))

        canvas.arc(centerX, centerY, radius, startAngle, endAngle, anticlockwise)

        canvas.restore(id)
    }

}