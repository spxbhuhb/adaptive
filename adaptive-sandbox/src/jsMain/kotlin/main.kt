/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ktor.withWebSocketTransport
import hu.simplexion.adaptive.lib.sandbox.SandboxExports
import hu.simplexion.adaptive.lib.sandbox.publicFun
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.common.browser.adapter.browser
import hu.simplexion.adaptive.ui.common.fragment.group
import hu.simplexion.adaptive.ui.common.fragment.pixel
import hu.simplexion.adaptive.ui.common.fragment.stack
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

val counterService = getService<CounterApi>()

fun now() = Clock.System.now()

fun main() {

    withJson()
    //withWebSocketTransport()

    browser(SandboxExports) {
        val time = poll(1.seconds, now()) { now() }

        pixel {

            stack(BoundingRect(50f, 50f, 400f, 50f)) {
                text("> ")
                if (time.toLocalDateTime(TimeZone.currentSystemDefault()).second % 2 == 1) {
                    text("what an odd second")
                }
                text(" <")
            }

            //counterWithTime(time)

            text("$time", BoundingRect(150f, 150f, 250f, 20f))

            group(BoundingRect(200f, 200f, 200f, 200f)) {
                text("Hello World at 200!")
            }

        }
    }

}


@Adaptive
fun counterWithTime(time : Instant) {
    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter", BoundingRect(150f, 150f, 250f, 20f))
}