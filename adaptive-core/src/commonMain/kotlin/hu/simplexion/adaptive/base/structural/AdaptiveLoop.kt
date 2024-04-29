/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.structural

import hu.simplexion.adaptive.base.*

class AdaptiveLoop<BT, IT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int,
) : AdaptiveFragment<BT>(adapter, parent, index, 2) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    @Suppress("UNCHECKED_CAST")
    val iterator
        get() = state[0] as Iterator<IT>

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<BT>

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    val placeholder: AdaptiveBridge<BT> = adapter.createPlaceholder()

    fun addAnonymous(iteratorValue: IT): AdaptiveFragment<BT> =
        AdaptiveAnonymous(adapter, this, 0, 1, builder).also {
            fragments.add(it)
            it.setStateVariable(0, iteratorValue)
        }

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {
        // anonymous fragment iterator is read only, and it is set during create
    }

    override fun create() {
        if (trace) trace("before-Create")
        patch()
        if (trace) trace("after-Create")
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("before-Mount", "bridge", bridge)

        bridge.add(placeholder)

        for (fragment in fragments) {
            fragment.mount(placeholder)
        }

        if (adapter.trace) trace("after-Mount", "bridge", bridge)
    }

    override fun genPatchInternal() {
        // TODO think about re-running iterators, we should not do that
        if (dirtyMask != 0) {
            patchStructure()
        } else {
            patchContent()
        }
    }

    fun patchStructure() {
        var index = 0
        for (loopVariable in iterator) {
            if (index >= fragments.size) {
                val f = addAnonymous(loopVariable)
                f.create()
                f.mount(placeholder)
            } else {
                fragments[index].also {
                    it.setStateVariable(0, loopVariable)
                    it.patch()
                }
            }
            index ++
        }
        while (index < fragments.size) {
            val f = fragments.removeLast()
            f.unmount(placeholder)
            f.dispose()
            index ++
        }
    }

    fun patchContent() {
        for (fragment in fragments) {
            fragment.patch()
        }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", "bridge", bridge)

        for (fragment in fragments) {
            fragment.unmount(placeholder)
        }

        bridge.remove(placeholder)
        if (trace) trace("after-Unmount", "bridge", bridge)
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")

        for (f in fragments) {
            f.dispose()
        }

        if (trace) trace("after-Dispose")
    }

    override fun stateToTraceString(): String {
        val s0 = state[0]?.let { it::class.simpleName ?: "<iterator>" }
        val s1 = state[1]?.toString()
        return "[$s0,$s1]"
    }

}