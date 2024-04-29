/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.css.grid_gap_24
import hu.simplexion.adaptive.dom.AdaptiveDOMAdapter
import hu.simplexion.adaptive.html.button
import hu.simplexion.adaptive.html.div
import hu.simplexion.adaptive.html.grid
import hu.simplexion.adaptive.html.text
import hu.simplexion.adaptive.base.producer.poll
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun main() {
    adaptive(AdaptiveDOMAdapter(window.document.body !!).also { it.trace = true }) {

        var counter = 0
        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }
        val states = poll(10.seconds, default = mock(counter)) { mock(counter) }

        grid("repeat(5,max-content)", "40px", grid_gap_24) {

            div { text(time.toString().replace("T", " ").substringBefore('.')) }
            div { text("Amount") }
            div { text("Time") }
            div { text("producer") }
            div { text("Case") }

            for (state in states) {
                machineState(state)
            }
        }

        button("Click to produce!") {
            counter = counter + 1
        }
    }
}

class MachineState(
    val machineNumber: Int,
    val producedLength: Int?,
    val productionLength: Int?,
    val elapsedTime: Int?,
    val productionTime: Int?,
    val workerId: String,
    val caseId: String
) {
    override fun toString(): String {
        return "MachineState($machineNumber $producedLength $productionLength $elapsedTime $productionTime)"
    }
}

fun Adaptive.machineState(state: MachineState) {
    div { text(state.machineNumber) }
    div { text(if (state.producedLength != null) "${state.producedLength} / ${state.productionLength} m" else "Áll") }
    div { text(if (state.elapsedTime != null) "${state.elapsedTime} / ${state.productionTime} min" else "-") }
    div { text(state.workerId) }
    div { text(state.caseId) }
}

fun mock(counter: Int): List<MachineState> {
    return listOf(
        MachineState(1, null, null, null, null, "-", "-"),
        MachineState(2, null, null, null, null, "-", "-"),
        MachineState(3, null, null, null, null, "-", "-"),
        MachineState(4, null, null, null, null, "-", "-"),
        MachineState(5, null, null, null, null, "-", "-"),
        MachineState(6, null, null, null, null, "-", "-"),
        MachineState(7, null, null, null, null, "-", "-"),
        MachineState(8, counter, 120, counter, 50, "34232", "500050")
    )
}