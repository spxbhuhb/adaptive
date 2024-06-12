/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.canvas

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.canvas
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

@AdaptiveActual(canvas)
open class AdaptiveCircle(
    adapter: AdaptiveCanvasAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveCanvasFragment(adapter, parent, index, -1, 0) {

    override fun draw(ctx : CanvasRenderingContext2D) {
        val centerX = 200.0
        val centerY = 200.0
        val radius = 100.0

        ctx.beginPath()
        ctx.arc(centerX, centerY, radius, 0.0, 2 * PI)
        ctx.fillStyle = "gray"
        ctx.fill()
    }

}