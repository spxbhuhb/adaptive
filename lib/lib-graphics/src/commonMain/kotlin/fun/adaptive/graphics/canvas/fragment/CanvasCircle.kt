/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import kotlin.math.PI

@AdaptiveActual(canvas)
open class CanvasCircle(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    val centerX: Double
        by stateVariable()

    val centerY: Double
        by stateVariable()

    val radius: Double
        by stateVariable()

    val startAngle: Double?
        by stateVariable()

    val endAngle: Double?
        by stateVariable()

    val anticlockwise: Boolean?
        by stateVariable()

    override fun drawInner() {
        canvas.arc(centerX, centerY, radius, startAngle ?: 0.0, endAngle ?: (2 * PI), anticlockwise ?: false)
    }

}