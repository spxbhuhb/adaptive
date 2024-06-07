/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.invoke
import hu.simplexion.adaptive.foundation.fragment.slot
import hu.simplexion.adaptive.lib.sandbox.SandboxLibExports
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser.browser
import hu.simplexion.adaptive.ui.common.browser.resource.withJsResources
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
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

    browser(SandboxLibExports, exports) {
        grid(colTemplate(100.dp, 1.fr), rowTemplate(1.fr)) {

            column(BackgroundColor(lightGray)) {
                button("Login", replace { login() })
                button("Sandbox", replace { stuff() })
                button("Chessboard", replace { chessBoard() })
            }

            slot("mainContent") { chessBoard() }

        }
    }

}

@Adaptive
fun button(label : String, vararg instructions: AdaptiveInstruction) {
    row(greenGradient, borderRadius, *center, Padding(top = 8.dp, bottom = 8.dp), onClick { instructions<Replace>() }) {
        text(label, white, textMedium, noSelect)
    }
}