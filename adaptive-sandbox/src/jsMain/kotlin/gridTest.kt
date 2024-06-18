/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.instruction.*

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