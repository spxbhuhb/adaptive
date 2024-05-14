/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.producer.poll
import hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport
import hu.simplexion.adaptive.sandbox.api.CounterApi
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.dom.AdaptiveDOMAdapter
import hu.simplexion.adaptive.ui.html.text
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

val counterService = getService<CounterApi>()

fun now() = Clock.System.now()

fun main() {
    defaultWireFormatProvider = JsonWireFormatProvider()
    defaultServiceCallTransport = BasicWebSocketServiceCallTransport(useTextFrame = true).also { it.start() }

    adaptive(AdaptiveDOMAdapter()) {
        val time = poll(1.seconds, now()) { now() }
        counterWithTime(time)
    }
}

@Adaptive
fun counterWithTime(time : Instant) {
    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter")
}