/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package old

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.slot
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import hu.simplexion.adaptive.wireformat.withJson

fun main() {

    withJson()
    //withWebSocketTransport()
    withJsResources()

    browser {

        var width = 375 // 375 // pixel: 393
        var height = 812 // 812 // pixel: 808 - 24 - 24 = 760

        grid(colTemplate(100.dp, 1.fr), rowTemplate(50.dp, 1.fr)) {

            row { }

            row {
                button("393 x 760", onClick { width = 393; height = 760 })
                button("375 x 812", onClick { width = 375; height = 812 })
            }

            column(BackgroundColor(lightGray)) {
                navButton("Login", replace { login() })
                navButton("Welcome", replace { welcome() })
                navButton("SVG", replace { svgTest() })
                navButton("Chessboard", replace { chessBoard() })
            }

            box(size((width + 2 + 16).dp, (height + 2 + 16).dp), name("box1")) {
                column(position(16.dp, 16.dp), size((width + 2).dp, (height + 2).dp), border(lightGray, 1.dp)) {
                    box(size(width.dp, height.dp), name("box2")) {
                        slot("mainContent") { svgTest() }
                    }
                }
            }
        }
    }

}


@Adaptive
fun navButton(label: String, vararg instructions: AdaptiveInstruction) {
    button(label, *instructions, onClick { instructions<Replace>() })
}
