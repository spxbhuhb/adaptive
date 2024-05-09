/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.success

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.testing.*

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {

    }

    return if (adapter.rootFragment::class.simpleName?.startsWith("AdaptiveRoot") == true) "OK" else "Fail"
}