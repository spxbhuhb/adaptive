package hu.simplexion.adaptive.example.ui/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.producer.poll
import hu.simplexion.adaptive.example.api.CounterApi
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.basic.text
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun Adaptive.counter() {

    var counter = 0
    val counterService = getService<CounterApi>()
    val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }

    text(time.toString())

}