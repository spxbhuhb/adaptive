/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*

private val black = Color(0x0u)

private val blueishBackground = backgroundColor(Color(0xB0C4DEu))
private val greenishBackground = backgroundColor(Color(0xB4E7B4u))
private val trace = Trace()

@Adaptive
fun layouts() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        column()
        row()
        grid1fr()
        gridMargin()
        gridPadding()
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
            text("AAA", greenishBackground)
            text("BBB", blueishBackground)
        }
    }
}

@Adaptive
private fun row() {
    layoutExample {
        row {
            gap(10.dp)
            text("AAA", greenishBackground)
            text("BBB", blueishBackground)
        }
    }
}

@Adaptive
private fun grid1fr() {
    layoutExample {
        grid {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
        }
    }
}


@Adaptive
private fun gridMargin() {
    layoutExample {
        grid {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
            margin(10.dp)
            border(black, 1.dp)
        }
    }
}

@Adaptive
private fun gridPadding() {
    layoutExample {
        grid(trace) {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
            margin(10.dp)
            border(black, 1.dp)
            padding(10.dp)

            box {
                blueishBackground
            }
        }
    }
}