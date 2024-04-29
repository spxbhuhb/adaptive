/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAdapterFactory

object AdaptiveTestAdapterFactory : AdaptiveAdapterFactory() {

    override fun accept(vararg args: Any?): AdaptiveAdapter<TestNode> {
        return AdaptiveTestAdapter()
    }

}