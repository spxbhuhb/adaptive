/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun Basic() {
    T0()
}

@Collect
object testFactory : AdaptiveFragmentFactory()

fun box() : String {

    val adapter = AdaptiveTestAdapter()
    val parent = AdaptiveT1(adapter, null, 0)

    val fragment = testFactory.newInstance("stuff.Basic", parent, 123)

    if (fragment.adapter != adapter) return "Fail: adapter"
    if (fragment.parent != parent) return "Fail: parent"
    if (fragment.declarationIndex != 123) return "Fail: index"

    return "OK"
}