/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package old/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.fragment.slot
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.withJsResources
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

val black = color(0x000000u)
val white = color(0xffffffu)
val lightGreen = color(0xA0DE6Fu)
val mediumGreen = color(0x53C282u)
val lightGray = color(0xd8d8d8u)
val mediumGray = color(0x666666u)
val purple = color(0xA644FFu)

val blackBackground = backgroundColor(black)
val greenGradient = leftToRightGradient(lightGreen, mediumGreen)
val borderRadius = borderRadius(8.dp)

val textSmall = fontSize(13.sp)
val textMedium = fontSize(15.sp)
val whiteBorder = border(white)
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

            row { }

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

            box(size((width + 2 + 16).dp, (height + 2 + 16).dp), name("box1")) {
                column(position(16.dp, 16.dp), size((width + 2).dp, (height + 2).dp), border(lightGray, 1.dp)) {
                    box(size(width.dp, height.dp), name("box2")) {
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