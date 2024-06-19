/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package layout

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.gap
import hu.simplexion.adaptive.ui.common.instruction.padding

@Adaptive
fun colTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        columnBasic()
    }
}

@Adaptive
private fun columnBasic() {
    layoutExample {
        column {
            gap(10.dp)
            text("AAA", layout.greenishBackground)
            text("BBB", layout.blueishBackground)
        }
    }
}