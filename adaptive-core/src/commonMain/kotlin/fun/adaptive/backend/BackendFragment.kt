/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.backend

import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.initStateMask
import `fun`.adaptive.utility.checkIfInstance

abstract class BackendFragment(
    adapter: BackendAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0, 3) {

    val backendAdapter
        get() = adapter as BackendAdapter

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {
        if (getThisClosureDirtyMask() != initStateMask) return true

        check(impl == null) { "inconsistent backend state innerMount with a non-null implementation" }

        (implFun.invoke()).also {
            impl = it
            it.fragment = this
            it.logger = backendAdapter.getLogger(it::class.simpleName!!) // FIXME using class simpleName
            it.create()
        }

        return true
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        // there is no actual UI for backend fragments
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        // there is no actual UI for backend fragments
    }

    // -------------------------------------------------------------------------
    // Implementation support
    // -------------------------------------------------------------------------

    // 0 : instructions

    val implFun : () -> BackendFragmentImpl
        get() = state[1].checkIfInstance()

    var impl : BackendFragmentImpl?
        get() = state[2].checkIfInstance()
        set(value) { state[2] = value }

}