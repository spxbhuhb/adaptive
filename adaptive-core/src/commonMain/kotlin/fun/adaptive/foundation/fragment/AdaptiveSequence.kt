/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.utility.checkIfInstance

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
        get() = get(0)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchInternal(): Boolean {
        // FIXME sequence genPatchInternal does not handle state changes properly
        if (children.size != indices.size) {
            for (itemIndex in indices) {
                createClosure.owner.genBuild(this, itemIndex, 0)?.let { children.add(it) }
            }
            return false
        }

        return true
    }

    override fun stateToTraceString(): String {
        return if (get<Any?>(0) != null) indices.contentToString() else "[]"
    }
}