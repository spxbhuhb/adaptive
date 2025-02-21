/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
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

@JsName("getInstructions")
fun instructions() : AdaptiveInstructionGroup {
    replacedByPlugin("gets the instructions of the declaring fragment")
}

fun AdaptiveFragment.throwAway() {
    if (isMounted) unmount()
    dispose()
}

fun AdaptiveFragment.throwChildrenAway() {
    children.forEach { it.throwAway() }
    children.clear()
}