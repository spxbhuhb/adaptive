/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.foundation.select.*
import hu.simplexion.adaptive.foundation.structural.*
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun login() {
    row {
        row {
            row {
                text("Label", onClick { click ++ })
            }
        }
    }
}

@Adaptive
fun row(@Adaptive block: () -> Unit) {
    block()
}

@Adaptive
fun text(content: String, vararg instructions: AdaptiveInstruction) {

}

var click = 0

class AdaptiveUIFragment<T>

class AdaptiveUIEvent(
    val fragment: AdaptiveUIFragment<*>,
    val nativeEvent: Any?
)

fun onClick(handler: (event: AdaptiveUIEvent) -> Unit) = OnClick(handler)

class OnClick(val handler: (event: AdaptiveUIEvent) -> Unit) : AdaptiveInstruction {

    fun execute() {
        click ++
    }

    override fun toString(): String {
        return "OnClick"
    }
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        login()
    }

    adapter.collect<OnClick>().forEach { it.execute() }

    if (click != 1) return "Fail: click = 0"
    return "OK"

}