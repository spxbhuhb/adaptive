/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
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
import `fun`.adaptive.lib.sandbox.withSandbox
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontName
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.leftToRightGradient
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.noTextWrap
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.route
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.slot
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.navigation.NavClick
import `fun`.adaptive.ui.platform.withJsResources


fun main() {

    withJsResources()

    //(trace = Trace(".*"))
    //, trace = trace("removeActual|.*-Unmount|setContent")
    browser(backend = backend(LocalServiceCallTransport()) { }) {
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
    leftToRightGradient(color(0xA0DE6Fu), color(0x53C282u)),
    cornerRadius(8.dp),
    AlignItems.Companion.startCenter,
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