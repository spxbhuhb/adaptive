/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.adaptive_sandbox.generated.resources.*
import hu.simplexion.adaptive.adaptive_sandbox.generated.resources.Res
import hu.simplexion.adaptive.adaptive_sandbox.generated.resources.background
import hu.simplexion.adaptive.adaptive_sandbox.generated.resources.logo
import hu.simplexion.adaptive.adaptive_sandbox.generated.resources.terms
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ktor.withWebSocketTransport
import hu.simplexion.adaptive.lib.sandbox.SandboxExports
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser.adapter.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

val counterService = getService<CounterApi>()

fun now() = Clock.System.now()

val black = Color(0x000000)
val white = Color(0xffffff)
val lightGreen = Color(0xA0DE6F)
val mediumGreen = Color(0x53C282)
val lightGray = Color(0xd8d8d8)
val mediumGray = Color(0x666666)
val purple = Color(0xA644FF)

val blackBackground = BackgroundColor(black)
val greenGradient = BackgroundGradient(90, lightGreen, mediumGreen)
val borderRadius = BorderRadius(8)

val textSmall = FontSize(13f)
val whiteBorder = Border(white)
val bold = FontWeight(700)
val smallWhiteNoWrap = arrayOf(white, textSmall, TextWrap.NoWrap)

val center = arrayOf<AdaptiveInstruction>(AlignItems.Center, JustifyContent.Center)
val traceLayout = Trace(Regex("layout"), Regex("measure.*"))

fun main() {

    withJson()
    withWebSocketTransport()

    browser(SandboxExports) {

        row {
            login()
        }

//
//        pixel {
//
//            stack(greenGradient, borderRadius, Frame(50f, 50f, 400f, 50f)) {
//
//                clickable(onClick = { console.log("hello") }) {
//                    text("> ")
//                    if (time.toLocalDateTime(TimeZone.currentSystemDefault()).second % 2 == 1) {
//                        text("what an odd second")
//                    }
//                    text(" <")
//                }
//
//            }
//
//            //counterWithTime(time)
//
//            text("$time", Frame(150f, 150f, 250f, 20f))
//
//            stack(Frame(200f, 200f, 200f, 200f)) {
//                text("Hello World at 200!")
//            }
//
//            grid(
//                ColTemplate(Repeat(2, 50.dp)),
//                RowTemplate(1.fr, 50.dp),
//                Frame(400f, 400f, 400f, 400f)
//            ) {
//                text("1", greenGradient, borderRadius, white)
//                text("2", grayBorder, borderRadius)
//                text("3")
//                text("4")
//            }
//
//        }
    }

}

@Adaptive
fun login() {
    box(Frame(0f, 0f, 375f, 812f)) {

        image(Res.drawable.background)

        grid(
            RowTemplate(260.dp, 1.fr, 100.dp, 100.dp),
            ColTemplate(1.fr)
        ) {

            row(AlignItems.End, JustifyContent.Center, Padding(bottom = 30f)) {
                image(Res.drawable.logo, Size(92f, 92f))
            }

            row(AlignItems.Start, JustifyContent.Center) {
                text("Good Morning", white, FontSize(40f), LetterSpacing(- 1f))
            }

            grid(
                RowTemplate(50.dp),
                ColTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)
            ) {

                row(2.gridCol, greenGradient, borderRadius, *center, onClick { println("Sign Up") }) {
                    text("Sign Up", white)
                }

                row(4.gridCol, whiteBorder, borderRadius, *center) {
                    text("Sign In", white)
                }

            }

            column(AlignItems.Center, Padding(right = 32f, left = 32f)) {
                row {
                    text("By joining you agree to our", *smallWhiteNoWrap, Padding(right = 6f))
                    text("Terms of Service", externalLink(Res.file.terms), *smallWhiteNoWrap, bold, Padding(right = 6f))
                    text("and", *smallWhiteNoWrap)
                }
                text("Privacy Policy", externalLink(Res.file.policy), *smallWhiteNoWrap, bold)
            }
        }
    }
}

@Adaptive
fun counterWithTime(time: Instant) {
    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter", Frame(150f, 150f, 250f, 20f))
}

fun chess(size : Int, cellSize : DIPixel) =
    arrayOf(ColTemplate(Repeat(size, cellSize)), RowTemplate(Repeat(size, cellSize)))

fun cellColor(r : Int, c: Int) =
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