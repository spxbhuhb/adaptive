/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure

class AdaptiveSequence(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, 1) {

    override val createClosure : AdaptiveClosure
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    val fragments = mutableListOf<AdaptiveFragment>()

    val indices : IntArray
        get() = state[0] as IntArray

    override fun create() {
        if (trace) trace("before-Create")

        patch()

        for (itemIndex in indices) {
            createClosure.owner.genBuild(this, itemIndex)?.let { fragments += it }
        }

        if (trace) trace("after-Create")
    }

    override fun mount() {
        if (trace) trace("before-Mount")
        fragments.forEach { it.mount() }
        if (trace) trace("after-Mount")
    }

    override fun genPatchInternal() {
        fragments.forEach { it.patch() }
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")
        fragments.forEach { it.unmount() }
        if (trace) trace("after-Unmount")
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")
        fragments.forEach { it.dispose() }
        if (trace) trace("after-Dispose")
    }

    override fun filter(result : MutableList<AdaptiveFragment>, filterFun : (it : AdaptiveFragment) -> Boolean) {
        if (filterFun(this)) {
            result += this
        }
        fragments.forEach {
            it.filter(result, filterFun)
        }
    }

    override fun stateToTraceString(): String {
        return if (state[0] != null) indices.contentToString() else "[]"
    }
}