/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package old

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
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
val cornerRadius = cornerRadius(8.dp)

val textSmall = fontSize(13.sp)
val textMedium = fontSize(15.sp)
val whiteBorder = border(white)
val bold = FontWeight(700)
val smallWhiteNoWrap = arrayOf(white, textSmall, TextWrap.NoWrap)

val traceAll = Trace(Regex(".*"))
val traceLayout = Trace(Regex("layout.*"), Regex("measure.*"))

val button = arrayOf(
    greenGradient,
    cornerRadius,
    AlignItems.center,
    Padding(8.dp),
    Height(50.dp)
)

@Adaptive
fun button(label: String, vararg instructions: AdaptiveInstruction) {
    row(*button, *instructions) {
        text(label, white, textMedium, noSelect)
    }
}