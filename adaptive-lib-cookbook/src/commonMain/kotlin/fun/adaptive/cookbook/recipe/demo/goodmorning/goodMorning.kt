/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.recipe.demo.goodmorning

import `fun`.adaptive.cookbook.and
import `fun`.adaptive.cookbook.app_name
import `fun`.adaptive.cookbook.background
import `fun`.adaptive.cookbook.by_joining
import `fun`.adaptive.cookbook.logo
import `fun`.adaptive.cookbook.policy
import `fun`.adaptive.cookbook.privacy_policy
import `fun`.adaptive.cookbook.sleepiness
import `fun`.adaptive.cookbook.snooze
import `fun`.adaptive.cookbook.terms
import `fun`.adaptive.cookbook.terms_of_service
import `fun`.adaptive.cookbook.what_an_odd_second
import `fun`.adaptive.cookbook.you_are_sleepy
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.resource.file.Files
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridCol
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.leftToRightGradient
import `fun`.adaptive.ui.api.letterSpacing
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.noTextWrap
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingBottom
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.paddingVertical
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.layout.Padding
import `fun`.adaptive.ui.instruction.text.FontWeight
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds


@Adaptive
fun mobileExample(@Adaptive body: () -> Unit) {
    val borderWidth = 1 + 1
    val width = 375 + borderWidth // 375 // pixel: 393
    val height = 812 + borderWidth // 812 // pixel: 808 - 24 - 24 = 760

    column {
        AlignItems.Companion.start
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

fun nowLocal() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

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
val smallWhiteNoWrap = instructionsOf(textColor(white), textSmall, noTextWrap)

val button = instructionsOf(
    greenGradient,
    cornerRadius,
    AlignItems.Companion.center,
    Padding(8.dp),
    Height(50.dp)
)

@Adaptive
fun button(label: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(button, instructions()) {
        text(label, textColor(white), textMedium, noSelect)
    }
    return fragment()
}

@Adaptive
fun goodMorning() : AdaptiveFragment {

    var counter = 0
    //val millis = poll(20.milliseconds) { Clock.System.now() } ?: Clock.System.now()
    val time = poll(1.milliseconds) { nowLocal() } ?: nowLocal()
    val timeText = "${time.hour.twoDigits}:${time.minute.twoDigits}:${time.second.twoDigits}"

    mobileExample {
        image(Images.background)

        grid {
            rowTemplate(140.dp, 50.dp, 1.fr, 1.fr, 1.fr, 50.dp, 100.dp)
            colTemplate(1.fr)

            logo()
            title()
            time(timeText)
            column {
                progress(time)
                //milliProgress(millis)
            }
            messages(time, counter)

            grid {
                maxSize
                rowTemplate(50.dp)
                colTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)

                row(2.gridCol, greenGradient, cornerRadius, AlignItems.Companion.center, onClick { counter ++ }) {
                    maxSize
                    text(Strings.snooze) .. textColor(white) .. textMedium .. noSelect
                }

                row(4.gridCol, whiteBorder, cornerRadius, AlignItems.Companion.center) {
                    maxSize
                    text("${Strings.sleepiness} $counter") .. textColor(white) .. textMedium
                }
            }

            terms()
        }
    }

    return fragment()
}

@Adaptive
private fun logo() {
    row {
        AlignItems.Companion.bottomCenter
        paddingBottom(20.dp)
        maxSize

        image(Images.logo, size(92.dp, 92.dp))
    }
}

@Adaptive
private fun title() {
    row {
        AlignItems.Companion.bottomCenter
        maxSize

        text(Strings.app_name, textColor(white), fontSize(40.sp), letterSpacing(- 0.02))
    }
}

@Adaptive
private fun time(timeText: String) {
    column {
        maxSize
        AlignItems.Companion.bottomCenter
        paddingTop(12.dp)

        text(timeText, textColor(white), fontSize(80.sp), letterSpacing(- 0.02))
    }
}

@Adaptive
private fun progress(time: LocalDateTime) {
    row {
        AlignItems.Companion.center
        maxSize

        for (i in 0 .. time.second) {
            text(if (i % 10 == 0) "|" else ".", textColor(white))
        }
    }
}

@Adaptive
private fun milliProgress(instant: Instant) {
    row {
        AlignItems.Companion.center
        maxSize

        for (i in 0 .. (instant.nanosecondsOfSecond / (20 * 1_000_000))) {
            box(greenGradient) {
                text(if (i % 10 == 0) "|" else ".", textColor(white))
            }
        }
    }
}

@Adaptive
private fun messages(time: LocalDateTime, counter: Int) {
    column {
        AlignItems.Companion.center
        maxWidth
        gap { 10.dp }

        if (time.second % 2 == 1) {
            row(greenGradient, cornerRadius) {
                paddingHorizontal { 16.dp } .. paddingVertical { 8.dp }

                text(Strings.what_an_odd_second, textColor(white))
            }
        }

        if (counter > 3) {
            row(greenGradient, cornerRadius, Padding(8.dp)) {
                text(Strings.you_are_sleepy, textColor(white), textMedium)
            }
        }
    }
}

@Adaptive
private fun terms() {
    column(AlignItems.Companion.center, padding(right = 32.dp, left = 32.dp, top = 12.dp)) {
        row {
            text(Strings.by_joining, smallWhiteNoWrap, paddingRight(6.dp)) .. noSelect
            text(Strings.terms_of_service, externalLink(Files.terms), smallWhiteNoWrap, bold, paddingRight(right = 6.dp)) .. noSelect
            text(Strings.and, smallWhiteNoWrap) .. noSelect
        }
        text(Strings.privacy_policy, externalLink(Files.policy), smallWhiteNoWrap, bold) .. noSelect
    }
}