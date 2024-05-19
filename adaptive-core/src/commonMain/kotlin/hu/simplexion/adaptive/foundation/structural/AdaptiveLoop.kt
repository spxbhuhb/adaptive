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

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    @Suppress("UNCHECKED_CAST")
    val iterator
        get() = state[0] as Iterator<IT>

    val builder
        get() = state[1] as BoundFragmentFactory

    fun addAnonymous(iteratorValue: IT): AdaptiveFragment =
        AdaptiveAnonymous(adapter, this, 0, 1, builder).also {
            children.add(it)
            it.setStateVariable(0, iteratorValue)
        }

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // anonymous fragment iterator is read only, and it is set during create
    }

    override fun addActual(fragment: AdaptiveFragment) {
        parent !!.addActual(fragment)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        parent !!.removeActual(fragment)
    }

    override fun genPatchInternal(): Boolean {
        // TODO think about re-running iterators, we should not do that
        if (dirtyMask != 0) {
            patchStructure()
        } else {
            patchContent()
        }
        return false
    }

    fun patchStructure() {
        var index = 0
        for (loopVariable in iterator) {
            if (index >= children.size) {
                val f = addAnonymous(loopVariable)
                f.create()
                f.mount()
            } else {
                children[index].also {
                    it.setStateVariable(0, loopVariable)
                    it.patch()
                }
            }
            index ++
        }
        while (index < children.size) {
            val f = children.removeLast()
            f.unmount()
            f.dispose()
            index ++
        }
    }

    fun patchContent() {
        children.forEach { it.patch() }
    }

    override fun stateToTraceString(): String {
        val s0 = state[0]?.let { it::class.simpleName ?: "<iterator>" }
        val s1 = state[1]?.toString()
        return "[$s0,$s1]"
    }

}