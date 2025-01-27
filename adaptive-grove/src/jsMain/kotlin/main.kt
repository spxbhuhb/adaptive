/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import adaptive_grove.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.backend.backend
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.designer.groveMain
import `fun`.adaptive.grove.fragment.GroveFragmentFactory
import `fun`.adaptive.grove.groveCommon
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.grove.ufd.ufdMain
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()
        groveCommon()

        commonMainStringsStringStore0.load()

        browser(backend = backend { }) { adapter ->

            adapter.fragmentFactory += arrayOf(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                GroveFragmentFactory,
                GroveRuntimeFragmentFactory
            )

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            ufdMain()
        }
    }
}