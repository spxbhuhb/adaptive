/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.TestedInPlugin
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.utility.checkIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

@AdaptiveActual
@TestedInPlugin
class FoundationSlot(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, - 1, 3) {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = createClosure

    val name: String?
        get() = state[0].checkIfInstance()

    val historySize: Int
        get() = state[1].checkIfInstance()

    val initialContent: BoundFragmentFactory
        get() = state[2].checkIfInstance()

    val backHistory = mutableListOf<List<AdaptiveFragment>>()

    val forwardHistory = mutableListOf<List<AdaptiveFragment>>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return initialContent.build(this)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // slot children are detached
        // the child state is set by the `setContent`
    }

    override fun genPatchInternal(): Boolean {
        val trace = instructions.firstOrNullIfInstance<Trace>()
        if (trace != null && trace.patterns.isNotEmpty()) {
            tracePatterns = trace.patterns
        }

        if (isInit) {
            // init history size if it's on default
            if (state[1] == null) state[1] = 0
        }

        // descendants are detached, so we should not propagate changes after the initial patch
        return isInit
    }

    fun setContent(origin: AdaptiveFragment, detachIndex: Int): AdaptiveFragment {
        saveOrDisposeChildren()
        forwardHistory.clear()

        val fragment = origin.genBuild(this, detachIndex)

        // genBuild calls `create`
        // `create` calls
        // - declaringFragment.genPatchDescendant
        // - genInternalPatch
        // so the state is correct at this point

        checkNotNull(fragment) { "${origin}.genBuild(this, $detachIndex) returned with null" }

        children.add(fragment)

        if (isMounted) fragment.mount()

        if (trace) trace("setContent", "origin: $origin, detachIndex: $detachIndex fragment=$fragment")

        return fragment
    }

    /**
     * Saves the children into the back history or disposes them if the history
     * is disabled ([historySize] == 0).
     */
    fun saveOrDisposeChildren() {
        if (children.isEmpty()) return

        if (isMounted) children.forEach { it.unmount() }

        if (historySize == 0) {

            children.forEach { it.dispose() }
            children.clear()

        } else {

            backHistory += children.toList()

            while (backHistory.size > historySize) {
                backHistory.removeFirst().forEach { it.dispose() }
            }

        }

        children.clear()
    }

    /**
     * Restore children from the back history if there are any.
     * Adds current children to the forward history.
     *
     * @return  true if children have been restored, false if the back history is empty
     */
    fun back(): Boolean {
        if (backHistory.isEmpty()) return false

        if (isMounted) children.forEach { it.unmount() }

        forwardHistory.add(0, children.toList())

        while (forwardHistory.size > historySize) {
            forwardHistory.removeLast().forEach { it.dispose() }
        }

        children.clear()
        children += backHistory.removeLast()

        if (isMounted) children.forEach { it.mount() }

        if (trace) trace("back", "${children.first()}")

        return true
    }

    /**
     * Restore children from the forward history if there are any.
     * Adds current children to the back history.
     *
     * @return  true if children have been restored, false if the forward history is empty
     */
    fun forward(): Boolean {
        if (forwardHistory.isEmpty()) return false

        if (isMounted) children.forEach { it.unmount() }

        backHistory.add(children.toList())

        while (backHistory.size > historySize) {
            backHistory.removeFirst().forEach { it.dispose() }
        }

        children.clear()
        children += forwardHistory.removeFirst()

        if (isMounted) children.forEach { it.mount() }

        if (trace) trace("forward", "${children.first()}")

        return true
    }
}