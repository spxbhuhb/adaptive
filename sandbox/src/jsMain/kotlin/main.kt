/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.designer.designerMain
import hu.simplexion.adaptive.designer.fragment.DesignerFragmentFactory
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.lib.sandbox.ui.graphics.svgExample
import hu.simplexion.adaptive.lib.sandbox.ui.layout.layoutMain
import hu.simplexion.adaptive.lib.sandbox.ui.markdown.markdown
import hu.simplexion.adaptive.lib.sandbox.ui.misc.chessBoard
import hu.simplexion.adaptive.lib.sandbox.ui.mobile.goodMorning
import hu.simplexion.adaptive.lib.sandbox.ui.mobile.mobileExample
import hu.simplexion.adaptive.lib.sandbox.ui.mobile.welcome
import hu.simplexion.adaptive.lib.sandbox.ui.navigation.slotOne
import hu.simplexion.adaptive.lib.sandbox.ui.navigation.slotTwo
import hu.simplexion.adaptive.lib.sandbox.ui.withSandbox
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.withJsResources

fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    //, trace = trace("removeActual|.*-Unmount|setContent")
    browser(DesignerFragmentFactory) {
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
                navButton("Hit Detect") .. navClick { hitDetect() }
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
                    route { hitDetect() }
                    route { designerMain() }

                    designerMain()

//                    text("Click on the left to load a demo!")
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
        text(label, textColor(0xffffffu), fontSize(15.sp), fontName("Noto Sans"), noSelect, noWrap)
    }
    return fragment()
}