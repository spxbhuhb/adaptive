/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.slot
import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.lib.sandbox.SandboxLibExports
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser.browser
import hu.simplexion.adaptive.ui.common.browser.resource.withJsResources
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val counterService = getService<CounterApi>()

val Int.twoDigits
    get() = toString().padStart(2, '0')

val Int.threeDigits
    get() = toString().padStart(3, '0')

fun now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val black = Color(0x000000)
val white = Color(0xffffff)
val lightGreen = Color(0xA0DE6F)
val mediumGreen = Color(0x53C282)
val lightGray = Color(0xd8d8d8)
val mediumGray = Color(0x666666)
val purple = Color(0xA644FF)

val blackBackground = BackgroundColor(black)
val greenGradient = BackgroundGradient(90, lightGreen, mediumGreen)
val borderRadius = BorderRadius(8.dp)

val textSmall = FontSize(13.sp)
val textMedium = FontSize(15.sp)
val whiteBorder = Border(white)
val bold = FontWeight(700)
val smallWhiteNoWrap = arrayOf(white, textSmall, TextWrap.NoWrap)

val center = arrayOf<AdaptiveInstruction>(AlignItems.Center, JustifyContent.Center)
val traceAll = Trace(Regex(".*"))
val traceLayout = Trace(Regex("layout.*"), Regex("measure.*"))


fun main() {

    withJson()
    //withWebSocketTransport()
    withJsResources()

    browser {

        var width = 375 // 375 // pixel: 393
        var height = 812 // 812 // pixel: 808 - 24 - 24 = 760

        grid(colTemplate(100.dp, 1.fr), rowTemplate(50.dp, 1.fr)) {

            row {  }

            row {
                button("393 x 760", onClick { width = 393; height = 760 })
                button("375 x 812", onClick { width = 375; height = 812 })
            }

            column(BackgroundColor(lightGray)) {
                navButton("Login", replace { login() })
                navButton("Welcome", replace { welcome() })
                navButton("Sandbox", replace { stuff() })
                navButton("Chessboard", replace { chessBoard() })
            }

            box(Size((width + 2 + 16).dp, (height + 2 + 16).dp), name("box1")) {
                column(Point(16.dp, 16.dp), Size((width + 2).dp, (height + 2).dp), Border(lightGray, 1.dp)) {
                    box(Size(width.dp, height.dp), name("box2")) {
                        slot("mainContent") { stuff() }
                    }
                }
            }
        }
    }

}

val button = arrayOf(
    greenGradient,
    borderRadius,
    *center,
    Padding(8.dp),
    Height(50.dp)
)

@Adaptive
fun navButton(label: String, vararg instructions: AdaptiveInstruction) {
    button(label, *instructions, onClick { instructions<Replace>() })
}

@Adaptive
fun button(label: String, vararg instructions: AdaptiveInstruction) {
    row(*button, *instructions) {
        text(label, white, textMedium, noSelect)
    }
}