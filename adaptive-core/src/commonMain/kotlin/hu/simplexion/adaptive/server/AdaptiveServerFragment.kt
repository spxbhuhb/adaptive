/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.AdaptiveSupportFunction
import hu.simplexion.adaptive.server.components.ServerFragmentImpl

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

    override fun genPatchInternal() = Unit

    // -------------------------------------------------------------------------
    // Implementation support
    // -------------------------------------------------------------------------

    val implFun : AdaptiveSupportFunction
        get() = state[0] as AdaptiveSupportFunction

    var impl : ServerFragmentImpl?
        get() = state[1] as ServerFragmentImpl?
        set(value) { state[1] = value }

}