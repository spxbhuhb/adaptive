/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.internal.BoundSupportFunction
import hu.simplexion.adaptive.base.internal.initStateMask
import hu.simplexion.adaptive.server.builtin.ServerFragmentImpl

abstract class AdaptiveServerFragment<BT>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 2) {

    val serverAdapter
        get() = adapter as AdaptiveServerAdapter<BT>

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() {
        if (getThisClosureDirtyMask() != initStateMask) return

        check(impl == null) { "inconsistent server state innerMount with a non-null implementation" }

        @Suppress("UNCHECKED_CAST")
        (implFun.invoke()).also {
            it as ServerFragmentImpl<BT>
            impl = it
            it.fragment = this
            it.logger = serverAdapter.getLogger(it::class.simpleName!!) // FIXME using class simpleName
            it.create()
        }
    }

    // -------------------------------------------------------------------------
    // Implementation support
    // -------------------------------------------------------------------------

    val implFun : BoundSupportFunction
        get() = state[0] as BoundSupportFunction

    @Suppress("UNCHECKED_CAST")
    var impl : ServerFragmentImpl<BT>?
        get() = state[1] as ServerFragmentImpl<BT>?
        set(value) { state[1] = value }

}