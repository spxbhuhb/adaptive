/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.AdaptiveBridge

open class AdaptiveServerPlaceholder<BT> : AdaptiveBridge<BT> {

    override val receiver : BT
        get() = throw UnsupportedOperationException()

    override fun remove(child: AdaptiveBridge<BT>) {

    }

    override fun replace(oldChild: AdaptiveBridge<BT>, newChild: AdaptiveBridge<BT>) {

    }

    override fun add(child: AdaptiveBridge<BT>) {

    }

}