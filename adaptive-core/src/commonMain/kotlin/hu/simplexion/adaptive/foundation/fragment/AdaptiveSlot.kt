/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.TestedInPlugin
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.utility.checkIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

@TestedInPlugin
class AdaptiveSlot(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0, 2) {

    override val createClosure : AdaptiveClosure
        get() = parent!!.thisClosure

    override val thisClosure = createClosure

    val name : String?
        get() = state[0].checkIfInstance()

    val initialContent: BoundFragmentFactory
        get() = state[1].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return initialContent.build(this)
    }
    
    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // slot children are detached
        // the child state is set by the `setContent`
    }

    override fun genPatchInternal() : Boolean {
        val trace = instructions.firstOrNullIfInstance<Trace>()
        if (trace != null && trace.patterns.isNotEmpty()) {
            tracePatterns = trace.patterns
        }

        // descendants are detached, so we should changes after the initial patch
        return isInit
    }

    @TestedInPlugin
    fun setContent(origin: AdaptiveFragment, detachIndex: Int) {
        if (trace) trace("setContent", "origin: $origin, detachIndex: $detachIndex")

        if (children.isNotEmpty()) {
            if (isMounted) children.forEach { it.unmount() }
            children.forEach { it.dispose() }
            children.clear()
        }

        val fragment = origin.genBuild(this, detachIndex)

        // genBuild calls `create`
        // `create` calls
        // - declaringFragment.genPatchDescendant
        // - genInternalPatch
        // so the state is correct at this point

        checkNotNull(fragment) { "${origin}.genBuild(this, $detachIndex) returned with null" }

        children.add(fragment)

        if (isMounted) {
            fragment.mount()
        }
    }

}