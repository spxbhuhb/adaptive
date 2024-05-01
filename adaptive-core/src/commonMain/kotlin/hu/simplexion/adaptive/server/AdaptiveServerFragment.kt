/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.AdaptiveSupportFunction
import hu.simplexion.adaptive.base.adaptiveInitStateMask
import hu.simplexion.adaptive.server.component.ServerFragmentImpl

abstract class AdaptiveServerFragment<BT,IT : ServerFragmentImpl<BT>>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 2) {

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() {
        if (getThisClosureDirtyMask() != adaptiveInitStateMask) return

        check(impl == null) { "inconsistent server state innerMount with a non-null implementation" }

        @Suppress("UNCHECKED_CAST")
        (implFun.invoke() as IT).also {
            impl = it
            it.fragment = this
            it.logger = (adapter as AdaptiveServerAdapter<BT>).getLogger(it.classFqName)
            it.create()
        }
    }

    // -------------------------------------------------------------------------
    // Implementation support
    // -------------------------------------------------------------------------

    val implFun : AdaptiveSupportFunction
        get() = state[0] as AdaptiveSupportFunction

    @Suppress("UNCHECKED_CAST")
    var impl : IT?
        get() = state[1] as IT?
        set(value) { state[1] = value }

}