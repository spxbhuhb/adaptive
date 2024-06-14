/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.grapics.canvas.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.grapics.canvas.ActualCanvas
import hu.simplexion.adaptive.grapics.canvas.CanvasAdapter
import hu.simplexion.adaptive.grapics.canvas.CanvasFragment
import hu.simplexion.adaptive.grapics.canvas.canvas

@AdaptiveExpect(canvas)
fun circle() {
    manualImplementation()
}

@AdaptiveActual(canvas)
open class CanvasCircle(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int
) : CanvasFragment(adapter, parent, index, -1, 0) {

    override fun draw(canvas : ActualCanvas) {
//        val centerX = 200.0
//        val centerY = 200.0
//        val radius = 100.0
//
//        ctx.beginPath()
//        ctx.arc(centerX, centerY, radius, 0.0, 2 * PI)
//        ctx.fillStyle = "gray"
//        ctx.fill()
    }

}