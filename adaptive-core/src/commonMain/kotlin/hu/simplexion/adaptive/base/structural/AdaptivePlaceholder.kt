/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.structural

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveBridge
import hu.simplexion.adaptive.base.AdaptiveFragment

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