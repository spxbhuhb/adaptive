/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ktor.withWebSocketTransport
import hu.simplexion.adaptive.lib.sandbox.SandboxExports
import hu.simplexion.adaptive.lib.sandbox.publicFun
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.dom.browser
import hu.simplexion.adaptive.ui.basic.text
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.w3c.dom.Node
import kotlin.time.Duration.Companion.seconds

val counterService = getService<CounterApi>()

fun now() = Clock.System.now()

fun main() {

    withJson()
    withWebSocketTransport()

    browser(SandboxExports as AdaptiveFragmentFactory<Node>) {
        val time = poll(1.seconds, now()) { now() }
        counterWithTime(time)
        publicFun()
    }

}

@Adaptive
fun counterWithTime(time : Instant) {
    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter")
}