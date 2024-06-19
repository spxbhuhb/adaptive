/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.row

@Adaptive
fun layoutMain() {
    row {
        gridTest()
        rowTest()
        colTest()
    }
}