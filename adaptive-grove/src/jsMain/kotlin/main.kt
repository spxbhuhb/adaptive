/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.grove.designer.groveMain
import `fun`.adaptive.grove.fragment.GroveFragmentFactory
import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.browser
import `fun`.adaptive.ui.common.instruction.sp
import `fun`.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    //, trace = trace("removeActual|.*-Unmount|setContent")
    browser(GroveFragmentFactory) { adapter ->
        adapter as AbstractCommonAdapter<*, *>

        with(adapter.defaultTextRenderData) {
            fontName = "Noto Sans"
            fontSize = 17.sp
        }

        groveMain()
    }
}