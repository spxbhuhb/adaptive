/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox

import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.sp

fun withSandbox(adapter: AbstractAuiAdapter<*, *>) {

    adapter.fragmentFactory += arrayOf(CanvasFragmentFactory, SvgFragmentFactory)

    with(adapter.defaultTextRenderData) {
        fontName = "Noto Sans"
        fontSize = 17.sp
    }
}