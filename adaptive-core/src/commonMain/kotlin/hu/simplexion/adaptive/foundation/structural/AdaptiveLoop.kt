/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory

class AdaptiveLoop<IT>(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, 2) {

    override val createClosure : AdaptiveClosure
        get() = parent!!.thisClosure

    @Suppress("UNCHECKED_CAST")
    val iterator
        get() = state[0] as Iterator<IT>

    val builder
        get() = state[1] as BoundFragmentFactory

    val fragments = mutableListOf<AdaptiveFragment>()

    val placeholder: AdaptiveFragment = adapter.createPlaceholder(this, -1)

    fun addAnonymous(iteratorValue: IT): AdaptiveFragment =
        AdaptiveAnonymous(adapter, this, 0, 1, builder).also {
            fragments.add(it)
            it.setStateVariable(0, iteratorValue)
        }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // anonymous fragment iterator is read only, and it is set during create
    }

    override fun addActual(fragment: AdaptiveFragment) {
        parent!!.addActual(fragment)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        parent!!.removeActual(fragment)
    }

    override fun create() {
        if (trace) trace("before-Create")
        patch()
        if (trace) trace("after-Create")
    }

    override fun mount() {
        if (adapter.trace) trace("before-Mount")

        placeholder.mount()

        for (fragment in fragments) {
            fragment.mount()
        }

        if (adapter.trace) trace("after-Mount")
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
                f.mount()
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
            f.unmount()
            f.dispose()
            index ++
        }
    }

    fun patchContent() {
        for (fragment in fragments) {
            fragment.patch()
        }
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        for (fragment in fragments) {
            fragment.unmount()
        }

        placeholder.unmount()

        if (trace) trace("after-Unmount")
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")

        for (f in fragments) {
            f.dispose()
        }

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
        val s0 = state[0]?.let { it::class.simpleName ?: "<iterator>" }
        val s1 = state[1]?.toString()
        return "[$s0,$s1]"
    }

}