/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package old/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import kotlinx.datetime.LocalDateTime
import sandbox.*
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun login() {

    var counter = 0
    val time = poll(1.seconds) { now() } ?: now()
    val timeText = "${time.hour.twoDigits}:${time.minute.twoDigits}:${time.second.twoDigits}"

    box {

        image(Res.drawable.background, dropShadow(black, 10.dp, 10.dp, 10.dp))

        grid {
            rowTemplate(140.dp, 50.dp, 1.fr, 1.fr, 1.fr, 50.dp, 100.dp)
            colTemplate(1.fr)

            logo()
            title()
            time(timeText)
            progress(time)
            messages(time, counter)

            grid {
                rowTemplate(50.dp)
                colTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)

                row(2.gridCol, greenGradient, cornerRadius, *center, onClick { counter ++ }) {
                    text("Snooze", white, textMedium, noSelect)
                }

                row(4.gridCol, whiteBorder, cornerRadius, *center) {
                    text("Sleepiness: $counter", white, textMedium)
                }
            }

            terms()
        }
    }
}

@Adaptive
private fun logo() {
    row(AlignItems.End, JustifyContent.Center, paddingBottom(20.dp)) {
        image(Res.drawable.logo, size(92.dp, 92.dp))
    }
}

@Adaptive
private fun title() {
    row(AlignItems.Start, JustifyContent.Center) {
        text("Good Morning", white, fontSize(40.sp), letterSpacing(- 0.02))
    }
}

@Adaptive
private fun time(timeText: String) {
    column(AlignItems.Center, JustifyContent.Start, padding(top = 12.dp)) {
        text(timeText, white, fontSize(80.sp), letterSpacing(- 0.02))
    }
}

@Adaptive
private fun progress(time: LocalDateTime) {
    row(*center) {
        for (i in 0 .. time.second) {
            text(if (i % 10 == 0) "|" else ".", white)
        }
    }
}

@Adaptive
private fun messages(time: LocalDateTime, counter: Int) {
    column(AlignItems.Center, JustifyContent.Center) {
        if (time.second % 2 == 1) {
            row(AlignItems.Start, JustifyContent.Center, greenGradient, cornerRadius, Padding(8.dp)) {
                text("What an odd second!", white)
            }
        }

        if (counter > 3) {
            row(greenGradient, cornerRadius, Padding(8.dp)) {
                text("You are really sleepy today!", white, textMedium)
            }
        }
    }
}

@Adaptive
private fun terms() {
    column(AlignItems.Center, padding(right = 32.dp, left = 32.dp, top = 12.dp)) {
        row {
            text("By joining you agree to our", *smallWhiteNoWrap, paddingRight(6.dp))
            text("Terms of Service", externalLink(Res.file.terms), *smallWhiteNoWrap, bold, paddingRight(right = 6.dp))
            text("and", *smallWhiteNoWrap)
        }
        text("Privacy Policy", externalLink(Res.file.policy), *smallWhiteNoWrap, bold)
    }
}