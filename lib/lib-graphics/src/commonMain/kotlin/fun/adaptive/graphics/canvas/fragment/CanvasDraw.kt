/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas

@AdaptiveActual(canvas)
open class CanvasDraw(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    val drawFun: ActualCanvas.() -> Unit
        by stateVariable()

    override fun drawInner() {
        drawFun(canvas)
    }

}