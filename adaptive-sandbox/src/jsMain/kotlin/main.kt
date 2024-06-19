/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.gap
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import layout.layoutMain

fun main() {

    withJsResources()

    browser {
        row {
            gap(10.dp)
            layoutMain()
        }
    }

}
