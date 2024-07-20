/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import kotlin.js.JsName

fun adapter(): AdaptiveAdapter {
    replacedByPlugin("gets the adapter of the fragment")
}

@JsName("getFragment") // to avoid compiler warning with the fragment package
fun fragment(): AdaptiveFragment {
    replacedByPlugin("gets the fragment")
}

fun <T : AdaptiveTransformInterface> thisState(): T {
    replacedByPlugin("gets the fragment as a transform interface")
}

operator fun AdaptiveFragment.rangeTo(instruction: AdaptiveInstruction): AdaptiveFragment {
    replacedByPlugin("adds the instruction to the instruction parameter value")
}

operator fun AdaptiveFragment.rangeTo(instructions: Array<out AdaptiveInstruction>): AdaptiveFragment {
    replacedByPlugin("adds the instruction to the instruction parameter value")
}

operator fun AdaptiveInstruction.rangeTo(instruction: AdaptiveInstruction): Array<AdaptiveInstruction> =
    arrayOf(this, instruction)

operator fun Array<AdaptiveInstruction>.rangeTo(instruction: AdaptiveInstruction): Array<AdaptiveInstruction> =
    this + instruction