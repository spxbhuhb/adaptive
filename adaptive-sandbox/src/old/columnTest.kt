/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

private val black = Color(0x0u)
private val outerBorder = Color(0xF08080u)
private val innerBorder = Color(0xFFBF00u)

private val blueishBackground = backgroundColor(Color(0xB0C4DEu))
private val greenishBackground = backgroundColor(Color(0xB4E7B4u))
private val trace = Trace()

@Adaptive
fun columnTest() {
    column(name("outer-column"), padding(10.dp)) {
        gap(10.dp)
        columnBasic()
    }
}

@Adaptive
private fun layoutExample(@Adaptive example: () -> Unit) {
    box(size(208.dp, 158.dp), border(outerBorder, 4.dp), name("example-container")) {
        example()
    }
}


@Adaptive
private fun columnBasic() {
    layoutExample {
        column {
            gap(10.dp)
            text("AAA", greenishBackground)
            text("BBB", blueishBackground)
        }
    }
}