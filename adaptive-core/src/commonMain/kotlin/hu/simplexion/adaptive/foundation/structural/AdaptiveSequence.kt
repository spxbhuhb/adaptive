/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.utility.checkIfInstance

class AdaptiveSequence(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, -1, 1) {

    override val createClosure : AdaptiveClosure
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    val indices : IntArray
        get() = state[0].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchInternal(): Boolean {
        // FIXME sequence genPatchInternal does not handle state changes properly
        if (children.size != indices.size) {
            for (itemIndex in indices) {
                createClosure.owner.genBuild(this, itemIndex)?.let { children.add(it) }
            }
            return false
        }

        return true
    }

    override fun stateToTraceString(): String {
        return if (state[0] != null) indices.contentToString() else "[]"
    }
}