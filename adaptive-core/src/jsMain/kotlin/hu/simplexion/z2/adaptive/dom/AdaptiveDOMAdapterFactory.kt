/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.dom

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAdapterFactory
import org.w3c.dom.Node

object AdaptiveDOMAdapterFactory : AdaptiveAdapterFactory() {

    override fun accept(vararg args: Any?): AdaptiveAdapter<*>? {
        if (args.isEmpty()) return AdaptiveDOMAdapter()

        args[0].let {
            if (it != null && it is Node) return AdaptiveDOMAdapter(it)
        }

        return null
    }

}