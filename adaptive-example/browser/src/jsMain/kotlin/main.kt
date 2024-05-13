/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.example.ui.counter
import hu.simplexion.adaptive.ui.dom.AdaptiveDOMAdapter

fun main() {
    adaptive(AdaptiveDOMAdapter()) {

//        var counter = 0
//        val counterService = getService<CounterApi>()
//        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }
//
//        div(display_flex, flex_direction_column, width_240_px) {
//
//            div { text("incremented $counter times(s)") }
//
//            div { text(time.toString()) }
//
//            button("Click to increment!") {
//                counter = counter + 1
//            }
//
//            launchButton("Save the counter!") {
//                counterService.put(counter)
//            }
//
//            launchButton("Restore the counter!") {
//                counter = counterService.get()
//            }
//        }

        counter()
    }
}