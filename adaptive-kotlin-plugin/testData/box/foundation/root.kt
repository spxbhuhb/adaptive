/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.base.success

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adaptive
import `fun`.adaptive.foundation.testing.*

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {

    }

    return if (adapter.rootFragment::class.simpleName?.startsWith("AdaptiveRoot") == true) "OK" else "Fail"
}