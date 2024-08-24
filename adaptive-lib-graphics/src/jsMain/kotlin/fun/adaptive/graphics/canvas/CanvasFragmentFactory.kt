/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.graphics.canvas.fragment.CanvasCanvas
import `fun`.adaptive.graphics.canvas.fragment.CanvasCircle
import `fun`.adaptive.graphics.canvas.fragment.CanvasSvg
import `fun`.adaptive.ui.AuiAdapter

object CanvasFragmentFactory : FoundationFragmentFactory() {
    init {
        add("canvas:canvas") { p,i -> CanvasCanvas(p.adapter as AuiAdapter, p, i) }
        add("canvas:circle") { p,i -> CanvasCircle(p.adapter as CanvasAdapter, p, i) }
        add("canvas:svg") { p,i -> CanvasSvg(p.adapter as CanvasAdapter, p, i) }
    }
}