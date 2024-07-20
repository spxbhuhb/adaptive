/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.grove.designer.groveMain
import hu.simplexion.adaptive.grove.fragment.GroveFragmentFactory
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.instruction.sp
import hu.simplexion.adaptive.ui.common.platform.withJsResources

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