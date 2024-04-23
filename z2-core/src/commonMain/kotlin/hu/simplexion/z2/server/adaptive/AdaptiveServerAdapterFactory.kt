/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.server.adaptive

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAdapterFactory

object AdaptiveServerAdapterFactory : AdaptiveAdapterFactory() {

    override fun accept(vararg args: Any?): AdaptiveAdapter<*>? {
        if (args.isEmpty()) return AdaptiveServerAdapter()
        return null
    }

}