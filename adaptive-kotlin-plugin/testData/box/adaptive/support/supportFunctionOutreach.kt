/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*

var a = 0

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    @Suppress("UNCHECKED_CAST")
    val adapter = adaptive {
        val b = 12
        S1 { a = it + b }
    } as AdaptiveAdapter<TestNode>

    val s1 = adapter.rootFragment.containedFragment as AdaptiveS1<TestNode>

    s1.s0.invoke(13)

    if (a != 25) return "Fail: a != 25"

    return "OK"
}