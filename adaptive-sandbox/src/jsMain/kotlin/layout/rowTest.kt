/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.gap
import hu.simplexion.adaptive.ui.common.instruction.padding

@Adaptive
fun rowTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        rowBasic()
        rowAlign(AlignItems.topCenter)
        rowAlign(AlignItems.endCenter)
        rowAlign(AlignItems.bottomCenter)
        rowAlign(AlignItems.startCenter)
    }
}

@Adaptive
private fun rowBasic() {
    layoutExample {
        row {
            gap(10.dp)
            text("AAA", greenishBackground)
            text("BBB", blueishBackground)
        }
    }
}

@Adaptive
private fun rowAlign(alignItems: AlignItems) {
    layoutExample {
        row(alignItems) {
            gap(10.dp)
            text("AAA", greenishBackground)
            text("BBB", blueishBackground)
        }
    }
}