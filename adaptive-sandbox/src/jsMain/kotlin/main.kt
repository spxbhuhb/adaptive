/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import graphics.svgExample
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.slot
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import layout.layoutMain
import markdown.markdown
import misc.chessBoard
import mobile.goodMorning
import mobile.welcome

fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    browser {
        grid {
            colTemplate(200.dp, 1.fr)
            rowTemplate(1.fr)

            column {
                padding(10.dp)
                gap(4.dp)

                navButton("Good Morning", replace { goodMorning() })
                navButton("Welcome", replace { welcome() })
                navButton("SVG", replace { svgExample() })
                navButton("Layouts", replace { layoutMain() })
                navButton("Chessboard", replace { chessBoard() })
                navButton("Markdown", replace { markdown() })
            }

            column {
                padding(10.dp)

                slot("mainContent") {
                    //text("Click on the left to load a demo!")
                    markdown()
                }
            }
        }
    }
}

val button = arrayOf(
    leftToRightGradient(color(0xA0DE6Fu), color(0x53C282u)),
    cornerRadius(8.dp),
    AlignItems.startCenter,
    paddingLeft(16.dp),
    Height(32.dp)
)

@Adaptive
fun navButton(label: String, vararg instructions: AdaptiveInstruction) {
    row(*button, onClick { instructions<Replace>() }, *instructions) {
        text(label, color(0xffffffu), fontSize(15.sp), noSelect)
    }
}