/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.graphics.canvas.fragment.*
import `fun`.adaptive.ui.AuiBrowserAdapter

object CanvasFragmentFactory : FoundationFragmentFactory() {
    init {
        add("canvas:canvas") { p, i, s -> CanvasCanvas(p.adapter as AuiBrowserAdapter, p, i) }
        add("canvas:circle") { p, i, s -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
        add("canvas:draw") { p, i, s -> CanvasDraw(p.adapter as CanvasAdapter, p, i) }
        add("canvas:line") { p, i, s -> CanvasLine(p.adapter as CanvasAdapter, p, i) }
        add("canvas:filltext") { p, i, s -> CanvasFillText(p.adapter as CanvasAdapter, p, i) }
        add("canvas:svg") { p, i, s -> CanvasSvg(p.adapter as CanvasAdapter, p, i) }
        add("canvas:path") { p, i, s -> CanvasPath(p.adapter as CanvasAdapter, p, i) }
        add("canvas:transform") { p, i, s -> CanvasTransform(p.adapter as CanvasAdapter, p, i) }
    }
}