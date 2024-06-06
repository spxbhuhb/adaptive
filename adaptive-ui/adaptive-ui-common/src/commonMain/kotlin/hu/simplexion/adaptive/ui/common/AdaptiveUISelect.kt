/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.structural.AdaptiveSelectLogic

class AdaptiveUISelect<RT, CRT : RT>(
    adapter: AdaptiveUIAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveUIStructuralFragment<RT, CRT>(adapter, parent, index, 2), AdaptiveSelectLogic {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchInternal(): Boolean = super<AdaptiveSelectLogic>.genPatchInternal()

    override fun makeBranch(): AdaptiveFragment? = createClosure.owner.genBuild(this, shownBranch)

}