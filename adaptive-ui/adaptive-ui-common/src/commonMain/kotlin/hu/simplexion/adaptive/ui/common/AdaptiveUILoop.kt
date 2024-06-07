/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.fragment.AdaptiveLoopLogic

class AdaptiveUILoop<RT, CRT : RT>(
    adapter: AdaptiveUIAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveUIStructuralFragment<RT, CRT>(adapter, parent, index, 2), AdaptiveLoopLogic<Any> {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = super<AdaptiveLoopLogic>.genPatchInternal()

    override fun stateToTraceString(): String = super<AdaptiveLoopLogic>.stateToTraceString()

    override fun makeAnonymous() = AdaptiveAnonymous(adapter, this, 0, 1, builder)

}
