/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment.structural

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.fragment.AdaptiveLoopLogic
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

class CommonLoop<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
) : CommonStructuralFragment<RT, CRT>(adapter, parent, index, 2), AdaptiveLoopLogic<Any> {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = super<AdaptiveLoopLogic>.genPatchInternal()

    override fun stateToTraceString(): String = super<AdaptiveLoopLogic>.stateToTraceString()

    override fun makeAnonymous() = AdaptiveAnonymous(this, 0, 1, builder)

}
