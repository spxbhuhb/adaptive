/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.foundation.testing.AdaptiveTestAdapter
import `fun`.adaptive.graphics.canvas.fragment.CanvasCircle
import `fun`.adaptive.graphics.canvas.fragment.CanvasFillText
import `fun`.adaptive.graphics.canvas.fragment.CanvasLine
import `fun`.adaptive.graphics.canvas.fragment.CanvasSvg

object CanvasFragmentFactory : FoundationFragmentFactory() {
    init {
        add("canvas:canvas") { p,i -> TestCanvasCanvas(p.adapter as AdaptiveTestAdapter, p, i) }
        add("canvas:circle") { p,i -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
        add("canvas:line") { p,i -> CanvasLine(p.adapter as CanvasAdapter, p, i) }
        add("canvas:filltext") { p,i -> CanvasFillText(p.adapter as CanvasAdapter, p, i) }
        add("canvas:svg") { p,i -> CanvasSvg(p.adapter as CanvasAdapter, p, i) }
    }
}