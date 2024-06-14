/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.graphics.canvas

import hu.simplexion.adaptive.grapics.canvas.ActualCanvas
import hu.simplexion.adaptive.grapics.canvas.ActualPath
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

class BrowserCanvas : ActualCanvas {
    val receiver = document.createElement("canvas") as HTMLCanvasElement

    override fun getPath(): ActualPath = BrowserPath()
}