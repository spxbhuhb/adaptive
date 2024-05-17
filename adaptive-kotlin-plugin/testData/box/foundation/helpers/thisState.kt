/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*

var actual : AdaptiveFragment? = null

interface TestState : AdaptiveTransformInterface

fun box() : String {

    val testAdapter = AdaptiveTestAdapter()

    adaptive(testAdapter) {
        actual = thisState()
    }

    val expected = testAdapter.rootFragment

    return if (expected === actual) "OK" else "Fail: returned fragment is not the same: $expected $actual"
    
}