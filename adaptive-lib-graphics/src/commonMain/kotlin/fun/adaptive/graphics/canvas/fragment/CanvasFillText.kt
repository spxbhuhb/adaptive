/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.ui.instruction.layout.OuterAlignment
import `fun`.adaptive.ui.instruction.layout.PopupAlign

@AdaptiveActual(canvas)
open class CanvasFillText(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    val x: Double
        by stateVariable()

    val y: Double
        by stateVariable()

    val text: String
        by stateVariable()

    val alignment: PopupAlign?
        by stateVariable()

    override fun drawInner() {
        val content = text

        val safeRenderData = renderData
        val safeAlignment = alignment

        if (safeRenderData == null || safeAlignment == null) {
            canvas.fillText(x, y, content)
            return
        }

        val measurement = canvas.measureText(safeRenderData, content)

        val ax = when (safeAlignment.horizontal) {
            OuterAlignment.Above -> x
            OuterAlignment.Below -> x
            OuterAlignment.Before -> x - measurement.width
            OuterAlignment.Start -> x
            OuterAlignment.Center -> x - measurement.width / 2
            OuterAlignment.End -> x
            OuterAlignment.After -> x + 1.0
            null -> x
        }

        val ay = when (safeAlignment.vertical) {
            OuterAlignment.Before -> y
            OuterAlignment.After -> y
            OuterAlignment.Above -> y - 1.0
            OuterAlignment.Start -> y
            OuterAlignment.Center -> y + (measurement.height) / 2 + measurement.baseline
            OuterAlignment.End -> y + measurement.height - 1.0
            OuterAlignment.Below -> y + measurement.height
            null -> y
        }


        canvas.fillText(ax, ay, content)
    }

}