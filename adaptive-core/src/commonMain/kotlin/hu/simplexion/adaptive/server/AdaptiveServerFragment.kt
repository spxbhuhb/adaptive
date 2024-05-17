/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundSupportFunction
import hu.simplexion.adaptive.foundation.internal.initStateMask
import hu.simplexion.adaptive.server.builtin.ServerFragmentImpl

abstract class AdaptiveServerFragment(
    adapter: AdaptiveServerAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 2) {

    val serverAdapter
        get() = adapter as AdaptiveServerAdapter

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() {
        if (getThisClosureDirtyMask() != initStateMask) return

        check(impl == null) { "inconsistent server state innerMount with a non-null implementation" }

        (implFun.invoke()).also {
            it as ServerFragmentImpl
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

    var impl : ServerFragmentImpl?
        get() = state[1] as ServerFragmentImpl?
        set(value) { state[1] = value }

}