/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.layout

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.instruction.layout.AlignItems

@Adaptive
fun layoutMain() {
    row {
        AlignItems.start

        gridTest()
        rowTest()
        colTest()
        flowBoxTest()
    }
}