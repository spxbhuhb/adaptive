import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@Adaptive
fun stuff() {
    var counter = 0

    column {
        text("click", onClick { counter += 1 })
        row {
            if (counter % 2 == 1) {
                text("odd")
            }
        }
    }
}

@Adaptive
fun counterWithTime(time: Instant) {
    val counter = poll(1000.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter", Frame(150.dp, 150.dp, 250.dp, 20.dp))
}
