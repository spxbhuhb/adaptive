/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.fragment.AdaptiveLoopLogic
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.ui.AbstractAuiAdapter

class AuiLoop<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
) : AuiStructural<RT, CRT>(adapter, parent, index, 2), AdaptiveLoopLogic<Any> {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {
        super<AdaptiveLoopLogic>.genPatchInternal()
        //renderData.layoutFragment?.layoutChange(this) // FIXME loop layout change
        return false
    }

    override fun stateToTraceString(): String = super<AdaptiveLoopLogic>.stateToTraceString()

    override fun makeAnonymous() = AdaptiveAnonymous(this, 0, 1, builder)

}
