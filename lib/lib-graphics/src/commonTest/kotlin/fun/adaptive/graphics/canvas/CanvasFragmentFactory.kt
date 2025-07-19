/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.graphics.canvas.fragment.CanvasCircle
import `fun`.adaptive.graphics.canvas.fragment.CanvasFillText
import `fun`.adaptive.graphics.canvas.fragment.CanvasCanvasLine
import `fun`.adaptive.graphics.canvas.fragment.CanvasSvg
import `fun`.adaptive.ui.testing.DensityIndependentTestAdapter

object CanvasFragmentFactory : FoundationFragmentFactory() {
    init {
        add("canvas:canvas") { p, i, s -> TestCanvasCanvas(p.adapter as DensityIndependentTestAdapter, p, i) }
        add("canvas:circle") { p, i, s -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
        add("canvas:line") { p, i, s -> CanvasCanvasLine(p.adapter as CanvasAdapter, p, i) }
        add("canvas:filltext") { p, i, s -> CanvasFillText(p.adapter as CanvasAdapter, p, i) }
        add("canvas:svg") { p, i, s -> CanvasSvg(p.adapter as CanvasAdapter, p, i) }
    }
}