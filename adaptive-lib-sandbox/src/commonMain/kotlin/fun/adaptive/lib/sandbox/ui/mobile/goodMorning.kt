/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.mobile

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.resource.file.Files
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridCol
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.letterSpacing
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
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
import `fun`.adaptive.ui.instruction.layout.Padding
import `fun`.adaptive.ui.instruction.text.FontName
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import sandbox.lib.*
import kotlin.time.Duration.Companion.milliseconds

@Adaptive
fun goodMorning() {

    var counter = 0
    //val millis = poll(20.milliseconds) { Clock.System.now() } ?: Clock.System.now()
    val time = poll(1.milliseconds) { nowLocal() } ?: nowLocal()
    val timeText = "${time.hour.twoDigits}:${time.minute.twoDigits}:${time.second.twoDigits}"

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
                text("Snooze") .. textColor(white) .. textMedium .. noSelect // .. onClick { println("Hello World!") }
            }

            row(4.gridCol, whiteBorder, cornerRadius, AlignItems.Companion.center) {
                maxSize
                text("Sleepiness: $counter") .. textColor(white) .. textMedium
            }
        }

        terms()
    }

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

        text("Good Morning", textColor(white), fontSize(40.sp), letterSpacing(- 0.02))
    }
}

@Adaptive
private fun time(timeText: String) {
    column {
        maxSize
        AlignItems.Companion.bottomCenter
        paddingTop(12.dp)

        text(timeText, textColor(white), fontSize(80.sp), letterSpacing(- 0.02), FontName("Noto Sans"))
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

                text("What an odd second!", textColor(white))
            }
        }

        if (counter > 3) {
            row(greenGradient, cornerRadius, Padding(8.dp)) {
                text("You are really sleepy today!", textColor(white), textMedium)
            }
        }
    }
}

@Adaptive
private fun terms() {
    column(AlignItems.Companion.center, padding(right = 32.dp, left = 32.dp, top = 12.dp)) {
        row {
            text("By joining you agree to our", *smallWhiteNoWrap, paddingRight(6.dp))
            text("Terms of Service", externalLink(Files.terms), *smallWhiteNoWrap, bold, paddingRight(right = 6.dp))
            text("and", *smallWhiteNoWrap)
        }
        text("Privacy Policy", externalLink(Files.policy), *smallWhiteNoWrap, bold)
    }
}