/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.utility.checkIfInstance

/**
 * A fragment that delegates all `gen` functions to the functions stored in the state.
 * If any of the functions are missing the call is simply a no-op.
 */
class FoundationDelegate(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, - 1, 3) {

    constructor(
        adapter: AdaptiveAdapter,
        parent: AdaptiveFragment?,
        index: Int,
        buildFun: (AdaptiveFragment.(parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment?)? = null,
        patchDescendantFun: (AdaptiveFragment.(fragment: AdaptiveFragment) -> Unit)? = null,
        patchInternalFun: (AdaptiveFragment.() -> Boolean)? = null
    ) : this(adapter, parent, index) {
        state[0] = buildFun
        state[1] = patchDescendantFun
        state[2] = patchInternalFun
    }

    val buildFun: (AdaptiveFragment.(parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment?)?
        get() = state[0].checkIfInstance()

    val patchDescendantFun: (AdaptiveFragment.(fragment: AdaptiveFragment) -> Unit)?
        get() = state[1].checkIfInstance()

    val patchInternalFun: (AdaptiveFragment.() -> Boolean)?
        get() = state[2].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return buildFun?.invoke(this, parent, declarationIndex)?.also { it.create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        patchDescendantFun?.invoke(this, fragment)
    }


    override fun genPatchInternal(): Boolean {
        return patchInternalFun?.invoke(this) ?: true
    }

}