/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    val adapter = adaptive {

    }

    return if (adapter.rootFragment::class.simpleName?.startsWith("AdaptiveRoot") == true) "OK" else "Fail"
}