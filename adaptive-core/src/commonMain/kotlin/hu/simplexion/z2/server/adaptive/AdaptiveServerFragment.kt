/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.server.adaptive

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment

abstract class AdaptiveServerFragment(
    adapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver>,
    parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>?,
    index: Int,
    stateSize : Int
) : AdaptiveFragment<AdaptiveServerBridgeReceiver>(adapter, parent, index, stateSize) {

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment<AdaptiveServerBridgeReceiver>, declarationIndex: Int): AdaptiveFragment<AdaptiveServerBridgeReceiver>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<AdaptiveServerBridgeReceiver>) = Unit

}