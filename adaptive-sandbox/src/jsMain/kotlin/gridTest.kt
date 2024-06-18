/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.instruction.*

private val black = Color(0x0u)
private val outerBorder = Color(0xF08080u)
private val innerBorder = Color(0xFFBF00u)

private val blueishBackground = backgroundColor(Color(0xB0C4DEu))
private val greenishBackground = backgroundColor(Color(0xB4E7B4u))
private val trace = Trace()

@Adaptive
fun gridTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        grid1fr()
        gridMargin()
        gridBorder()
        gridPadding()
        gridMarginBorderPadding()
    }
}

@Adaptive
private fun layoutExample(@Adaptive example: () -> Unit) {
    box(size(208.dp, 158.dp), border(outerBorder, 4.dp), name("example-container")) {
        example()
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

            box {
                blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridBorder() {
    layoutExample {
        grid(trace) {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
            border(innerBorder, 4.dp)

            box(trace) {
                blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridPadding() {
    layoutExample {
        grid {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
            padding(10.dp)

            box {
                blueishBackground
            }
        }
    }
}

@Adaptive
private fun gridMarginBorderPadding() {
    layoutExample {
        grid {
            rowTemplate(1.fr)
            colTemplate(1.fr)
            greenishBackground
            margin(10.dp)
            border(innerBorder, 4.dp)
            padding(10.dp)

            box {
                blueishBackground
            }
        }
    }
}