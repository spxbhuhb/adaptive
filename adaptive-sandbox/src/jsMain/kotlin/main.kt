/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.lib.sandbox.SandboxExports
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser.adapter.browser
import hu.simplexion.adaptive.ui.common.browser.resource.withJsResources
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.datetime.*
import sandbox.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
val traceLayout = Trace(Regex("layout"), Regex("measure.*"))

fun main() {

    withJson()
    //withWebSocketTransport()
    withJsResources()

    browser(SandboxExports, trace = traceAll) {

        row {
            login()
        }

    }

}

@Adaptive
fun login() {

    var counter = 0
    val time = poll(1.seconds, now()) { now() }
    val timeText = "${time.hour.twoDigits}:${time.minute.twoDigits}:${time.second.twoDigits}"
    val milliText = (time.nanosecond / 1_000_000).threeDigits

    box(Frame(0.dp, 0.dp, 393.dp, (808 - 24 - 24).dp)) { // android Pixel 3 dimensions

        image(Res.drawable.background)

        grid(
            RowTemplate(140.dp, 50.dp, 1.fr, 1.fr, 1.fr, 50.dp, 100.dp),
            ColTemplate(1.fr)
        ) {
            logo()
            title()
            time(timeText)
            progress(time)
            messages(time, counter)

            grid(
                RowTemplate(50.dp),
                ColTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)
            ) {

                row(2.gridCol, greenGradient, borderRadius, *center, onClick { counter++ }) {
                    text("Snooze", white, textMedium, noSelect)
                }

                row(4.gridCol, whiteBorder, borderRadius, *center) {
                    text("Sleepiness: $counter", white, textMedium)
                }
            }

            terms()
        }
    }
}

@Adaptive
private fun logo() {
    row(AlignItems.End, JustifyContent.Center, Padding(bottom = 20.dp)) {
        image(Res.drawable.logo, Size(92.dp, 92.dp))
    }
}

@Adaptive
private fun title() {
    row(AlignItems.Start, JustifyContent.Center) {
        text("Good Morning", white, FontSize(40.sp), LetterSpacing(- 0.02f))
    }
}

@Adaptive
private fun time(timeText : String) {
    column(AlignItems.Center, JustifyContent.Start, Padding(top = 12.dp)) {
        text(timeText, white, FontSize(80.sp), LetterSpacing(- 0.02f))
    }
}

@Adaptive
private fun progress(time : LocalDateTime) {
    row(*center) {
       for (i in 0 .. time.second) {
           text(if (i % 10 == 0) "|" else ".", white)
       }
    }
}

@Adaptive
private fun messages(time : LocalDateTime, counter : Int) {
    column(AlignItems.Center, JustifyContent.Center) {
        if (time.second % 2 == 1) {
            row(AlignItems.Start, JustifyContent.Center, greenGradient, borderRadius, Padding(8.dp)) {
                text("What an odd second!", white)
            }
        }

        if (counter > 3) {
            row(greenGradient, borderRadius, Padding(8.dp)) {
                text("You are really sleepy today!", white, textMedium)
            }
        }
    }
}

@Adaptive
private fun terms() {
    column(AlignItems.Center, Padding(right = 32.dp, left = 32.dp, top = 12.dp)) {
        row {
            text("By joining you agree to our", *smallWhiteNoWrap, Padding(right = 6.dp))
            text("Terms of Service", externalLink(Res.file.terms), *smallWhiteNoWrap, bold, Padding(right = 6.dp))
            text("and", *smallWhiteNoWrap)
        }
        text("Privacy Policy", externalLink(Res.file.policy), *smallWhiteNoWrap, bold)
    }
}

@Adaptive
fun counterWithTime(time: Instant) {
    val counter = poll(1000.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter", Frame(150.dp, 150.dp, 250.dp, 20.dp))
}

fun chess(size: Int, cellSize: DPixel) =
    arrayOf(ColTemplate(Repeat(size, cellSize)), RowTemplate(Repeat(size, cellSize)))

fun cellColor(r: Int, c: Int) =
    if (((r * 8) + c) % 2 == 0) white else black

@Adaptive
fun chessBoard() {
    grid(*chess(8, 20.dp)) {
        for (r in 1 .. 8) {
            for (c in 1 .. 8) {
                row(cellColor(r, c)) { text(r * 8 + c) }
            }
        }
    }
}