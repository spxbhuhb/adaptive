/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*

private val black = Color(0x0u)

private val cyan = Color(0x32e3dau)
private val cyanBackground = backgroundColor(cyan)
private val orange = Color(0xfcba03u)
private val orangeBackground = backgroundColor(orange)

@Adaptive
fun layouts() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        column()
        row()
        grid1fr()
    }
}

@Adaptive
private fun layoutExample(@Adaptive example: () -> Unit) {
    box(size(202.dp, 152.dp), border(black, 1.dp), name("example-container")) {
        example()
    }
}


@Adaptive
private fun column() {
    layoutExample {
        column {
            gap(10.dp)
            text("AAA", orangeBackground)
            text("BBB", cyanBackground)
        }
    }
}

@Adaptive
private fun row() {
    layoutExample {
        row {
            gap(10.dp)
            text("AAA", orangeBackground)
            text("BBB", cyanBackground)
        }
    }
}

@Adaptive
private fun grid1fr() {
    layoutExample {
        grid {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            orangeBackground
        }
    }
}
