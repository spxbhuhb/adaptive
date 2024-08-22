/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.invoke
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.lib.sandbox.ui.graphics.svgExample
import `fun`.adaptive.lib.sandbox.ui.layout.layoutMain
import `fun`.adaptive.lib.sandbox.ui.markdown.markdown
import `fun`.adaptive.lib.sandbox.ui.misc.chessBoard
import `fun`.adaptive.lib.sandbox.ui.mobile.goodMorning
import `fun`.adaptive.lib.sandbox.ui.mobile.mobileExample
import `fun`.adaptive.lib.sandbox.ui.mobile.welcome
import `fun`.adaptive.lib.sandbox.ui.navigation.slotOne
import `fun`.adaptive.lib.sandbox.ui.navigation.slotTwo
import `fun`.adaptive.lib.sandbox.ui.withSandbox
import `fun`.adaptive.ui.common.browser
import `fun`.adaptive.ui.common.fragment.*
import `fun`.adaptive.ui.common.instruction.*
import `fun`.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    //, trace = trace("removeActual|.*-Unmount|setContent")
    browser {
        withSandbox(it) // to set default font name

        grid {
            maxSize .. colTemplate(200.dp, 1.fr)

            column {
                maxSize .. padding(10.dp) .. gap(4.dp)

                navButton("Good Morning") .. navClick { mobileExample { goodMorning() } }
                navButton("Welcome") .. navClick { welcome(true) }
                navButton("SVG") .. navClick { svgExample() }
                navButton("Layouts") .. navClick { layoutMain() }
                navButton("Chessboard") .. navClick { chessBoard() }
                navButton("Markdown") .. navClick { markdown() }
                navButton("Slot One") .. navClick { slotOne() }
                navButton("Slot Two") .. navClick { slotTwo() }
            }

            column {
                maxSize .. verticalScroll .. padding(10.dp)

                slot {
                    route { goodMorning() }
                    route { layoutMain() }
                    route { welcome(true) }
                    route { chessBoard() }
                    route { svgExample() }
                    route { markdown() }
                    route { slotOne() }
                    route { slotTwo() }

                    text("Click on the left to load a demo!")
                }
            }
        }
    }
//        .also {
//            println(it.first<AbstractGrid<*, *>>().dumpLayout(""))
//        }
}

val button = instructionsOf(
    maxWidth,
    leftToRightGradient(Color(0xA0DE6Fu), Color(0x53C282u)),
    cornerRadius(8.dp),
    AlignItems.startCenter,
    paddingLeft(16.dp),
    Height(32.dp)
)

@Adaptive
fun navButton(label: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*button, onClick { instructions<NavClick>() }, *instructions) {
        text(label, textColor(0xffffffu), fontSize(15.sp), fontName("Noto Sans"), noSelect, noTextWrap)
    }
    return fragment()
}