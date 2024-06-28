/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import graphics.svgExample
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.foundation.query.first
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.lib.sandbox.misc.chessBoard
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import hu.simplexion.adaptive.ui.common.support.layout.AbstractGrid
import layout.layoutMain
import markdown.markdown
import mobile.goodMorning
import mobile.welcome
import navigation.slotOne
import navigation.slotTwo

fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    browser {
        grid {
            fillSpace .. colTemplate(200.dp, 1.fr)

            column {
                fillSpace .. padding(10.dp) .. gap(4.dp)

                navButton("Good Morning") .. navClick { goodMorning() }
                navButton("Welcome") .. navClick { welcome() }
                navButton("SVG") .. navClick { svgExample() }
                navButton("Layouts") .. navClick { layoutMain() }
                navButton("Chessboard") .. navClick { chessBoard() }
                navButton("Markdown") .. navClick { markdown() }
                navButton("Slot One") .. navClick { slotOne() }
                navButton("Slot Two") .. navClick { slotTwo() }
            }

            column {
                fillSpace .. verticalScroll .. padding(10.dp)

                slot {
                    route { goodMorning() }
                    route { layoutMain() }
                    route { welcome() }
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
        .also {
            println(it.first<AbstractGrid<*, *>>().dumpLayout(""))
        }
}

val button = arrayOf(
    fillHorizontal,
    leftToRightGradient(color(0xA0DE6Fu), color(0x53C282u)),
    cornerRadius(8.dp),
    AlignItems.startCenter,
    paddingLeft(16.dp),
    Height(32.dp)
)

@Adaptive
fun navButton(label: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*button, onClick { instructions<NavClick>() }, *instructions) {
        text(label, color(0xffffffu), fontSize(15.sp), fontName("Noto Sans"), noSelect, TextWrap.NoWrap)
    }
    return fragment()
}