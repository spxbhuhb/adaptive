/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveBridge
import hu.simplexion.adaptive.foundation.AdaptiveFragment

class AdaptivePlaceholder<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 0) {

    val bridge = adapter.createPlaceholder()

    override fun create() {

    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun patchInternal() {

    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun dispose() {

    }

}