/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.datetime.datePicker
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(CanvasFragmentFactory, SvgFragmentFactory, GroveRuntimeFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            box {
                maxSize .. padding { 16.dp }

                datePicker()
            }

        }
    }
}
