/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveBridge
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.AdaptiveFragment

class AdaptiveSequence<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int,
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.components + this,
        createClosure.closureSize + state.size
    )

    val fragments = mutableListOf<hu.simplexion.adaptive.foundation.AdaptiveFragment<BT>>()

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

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Mount", "bridge", bridge)
        fragments.forEach { it.mount(bridge) }
        if (trace) trace("after-Mount", "bridge", bridge)
    }

    override fun genPatchInternal() {
        fragments.forEach { it.patch() }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", "bridge", bridge)
        fragments.forEach { it.unmount(bridge) }
        if (trace) trace("after-Unmount", "bridge", bridge)
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")
        fragments.forEach { it.dispose() }
        if (trace) trace("after-Dispose")
    }

    override fun filter(result : MutableList<hu.simplexion.adaptive.foundation.AdaptiveFragment<BT>>, filterFun : (it : AdaptiveFragment<BT>) -> Boolean) {
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