/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.grove.designer.groveMain
import `fun`.adaptive.grove.fragment.GroveFragmentFactory
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.sp

val clientBackend = backend {  }

fun main() {

    //(trace = Trace(".*"))
    //, trace = trace("removeActual|.*-Unmount|setContent")
    browser(GroveFragmentFactory, backend = clientBackend) { adapter ->

        with(adapter.defaultTextRenderData) {
            fontName = "Open Sans"
            fontSize = 16.sp
        }

        groveMain()
    }
}