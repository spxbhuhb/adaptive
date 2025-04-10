/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.internal.cleanStateMask

/**
 * Provides logic for fragments that are used in `for` structure. These functions
 * are in this interface instead of [AdaptiveLoop] so that other fragments can
 * use them easily without re-implementing again.
 */
interface AdaptiveLoopLogic<IT> {

    fun makeAnonymous() : AdaptiveAnonymous

    val state : Array<Any?>

    val dirtyMask : Int

    val children : MutableList<AdaptiveFragment>

    val isMounted : Boolean

    @Suppress("UNCHECKED_CAST")
    val iterator
        get() = state[1] as Iterator<IT>

    val builder
        get() = state[2] as BoundFragmentFactory

    fun genPatchInternal() : Boolean {
        if (dirtyMask != cleanStateMask) {
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
                if (isMounted) f.mount()
            } else {
                children[index].also {
                    it.setStateVariable(1, loopVariable)
                    it.patch()
                }
            }
            index ++
        }

        val originalSize = children.size

        while (index < originalSize) {
            val f = children.removeLast()
            f.throwAway()
            index ++
        }
    }

    fun patchContent() {
        children.forEach { it.patch() }
    }

    fun addAnonymous(iteratorValue: IT): AdaptiveFragment =
        makeAnonymous().also {
            children.add(it)
            it.setStateVariable(1, iteratorValue)
        }

    fun stateToTraceString(): String {
        val i = state[0]
        val s0 = state[1]?.let { it::class.simpleName ?: "<iterator>" }
        val s1 = state[2]?.toString()
        return "[$i,$s0,$s1]"
    }

}

class AdaptiveLoop<IT>(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, 3), AdaptiveLoopLogic<IT> {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = super<AdaptiveLoopLogic>.genPatchInternal()

    override fun makeAnonymous() = AdaptiveAnonymous(this, 0, 2, builder)

    // ---- Development support --------------------------------------------

    override fun stateToTraceString(): String =
        super<AdaptiveLoopLogic>.stateToTraceString()

}