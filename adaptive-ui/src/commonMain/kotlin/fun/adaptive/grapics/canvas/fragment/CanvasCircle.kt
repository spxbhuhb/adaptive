/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grapics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grapics.canvas.CanvasAdapter
import `fun`.adaptive.grapics.canvas.CanvasFragment
import `fun`.adaptive.grapics.canvas.canvas

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

    override fun draw() {
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