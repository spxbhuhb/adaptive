/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.button.api.buttonTheme
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()

        browser(CanvasFragmentFactory, SvgFragmentFactory, backend = backend { }, trace = traceAll) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            var a = false

            column {
                mybutton("Hello") .. onClick { a = true }
                if (a) {
                    text("World")
                }
            }

        }
    }
}

@Adaptive
fun mybutton(label: String, icon: GraphicsResourceSet? = null, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(buttonTheme.container, instructions()) {
        if (icon != null) svg(icon) .. buttonTheme.icon
        text(label) .. buttonTheme.text
    }
    return fragment()
}
