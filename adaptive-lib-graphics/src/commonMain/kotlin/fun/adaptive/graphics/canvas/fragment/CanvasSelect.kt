/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveSelectLogic
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.graphics.canvas.CanvasAdapter

class CanvasSelect(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : CanvasStructural(adapter, parent, index, 3), AdaptiveSelectLogic {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchInternal(): Boolean = super<AdaptiveSelectLogic>.genPatchInternal()

    override fun makeBranch(): AdaptiveFragment? = createClosure.owner.genBuild(this, shownBranch, 0)

}