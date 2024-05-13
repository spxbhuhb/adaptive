/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.producer.poll
import hu.simplexion.adaptive.example.api.CounterApi
import hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.dom.AdaptiveDOMAdapter
import hu.simplexion.adaptive.ui.html.text
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

val counterService = getService<CounterApi>()
fun now() = Clock.System.now()

fun main() {

    defaultServiceCallTransport = BasicWebSocketServiceCallTransport()

    adaptive(AdaptiveDOMAdapter()) {

        val counter = poll(1.seconds, 0) {
            counterService.incrementAndGet().also {
                println("Counter received $it")
            }
        }
        val time = poll(1.seconds, default = now()) { now() }

        text("$time $counter")

    }

}