/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.instruction.AlignItems

@Adaptive
fun layoutMain() {
    row {
        AlignItems.start

        gridTest()
        rowTest()
        colTest()
    }
}