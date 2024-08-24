/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas

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