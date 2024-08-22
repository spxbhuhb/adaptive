/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.mobile

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.common.fragment.box
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Adaptive
fun mobileExample(@Adaptive body: () -> Unit) {
    val borderWidth = 1 + 1
    val width = 375 + borderWidth // 375 // pixel: 393
    val height = 812 + borderWidth // 812 // pixel: 808 - 24 - 24 = 760

    column {
        AlignItems.start
        gap(10.dp)

        box {
            size(width.dp, height.dp)
            border(lightGray, 1.dp)

            body()
        }
    }
}

val Int.twoDigits
    get() = toString().padStart(2, '0')

val Int.threeDigits
    get() = toString().padStart(3, '0')

fun now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val black = Color(0x000000u)
val white = Color(0xffffffu)
val lightGreen = Color(0xA0DE6Fu)
val mediumGreen = Color(0x53C282u)
val lightGray = Color(0xd8d8d8u)
val mediumGray = Color(0x666666u)
val purple = Color(0xA644FFu)

val blackBackground = backgroundColor(black)
val greenGradient = leftToRightGradient(lightGreen, mediumGreen)
val cornerRadius = cornerRadius(8.dp)

val textSmall = fontSize(13.sp)
val textMedium = fontSize(15.sp)
val whiteBorder = border(white)
val bold = FontWeight(700)
val smallWhiteNoWrap = instructionsOf(textColor(white), textSmall, noTextWrap)

val button = instructionsOf(
    greenGradient,
    cornerRadius,
    AlignItems.center,
    Padding(8.dp),
    Height(50.dp)
)

@Adaptive
fun button(label: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*button, *instructions) {
        text(label, textColor(white), textMedium, noSelect)
    }
    return fragment()
}