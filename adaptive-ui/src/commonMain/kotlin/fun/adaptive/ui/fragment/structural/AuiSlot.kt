/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.adat.api.copy
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.navigation.HistorySize
import `fun`.adaptive.ui.instruction.navigation.Route
import `fun`.adaptive.ui.support.navigation.NavSegmentProducer
import `fun`.adaptive.ui.support.navigation.navSegment
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance

/**
 * @property  navSegment  When navigation is active (route instructions added) this internal state variable
 *                        contains the current nav segment produced by [navSegment].
 */
@AdaptiveActual
class AuiSlot(
    adapter: AbstractAuiAdapter<*, *>,
    parent: AdaptiveFragment?,
    index: Int
) : AdaptiveFragment(adapter, parent, index, INSTRUCTIONS, 3) {

    companion object {
        private const val INSTRUCTIONS = 0
        private const val DEFAULT_CONTENT = 1
        private const val NAV_SEGMENT = 2
    }

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = createClosure

    val uiAdapter = adapter

    // 0 is instructions

    val defaultContent: BoundFragmentFactory
        get() = state[DEFAULT_CONTENT].checkIfInstance()

    /**
     * [navSegment] may change when [setContent] is called or when the producer produces a
     * new value.
     */
    var navSegment: String?
        get() = state[NAV_SEGMENT].checkIfInstance()
        set(v) {
            state[NAV_SEGMENT] = v
        }

    var name: Name = Name.ANONYMOUS

    var historySize = 0

    var activeSegment: String? = null

    var routes = emptyList<Route>()

    val backHistory = mutableListOf<List<AdaptiveFragment>>()

    val forwardHistory = mutableListOf<List<AdaptiveFragment>>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return null // content handled in `genPatchInternal`
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

        if (haveToPatch(dirtyMask, 1 shl instructionIndex)) {
            // Added copy to cover use cases like `val something = route { somethingFun() }`
            // FIXME remove `slot` from `Route` and keep `Route` immutable
            routes = instructions.filterIsInstance<Route>().map { it.copy() }.onEach { it.slot = this }

            historySize = instructions.firstOrNullIfInstance<HistorySize>()?.size ?: 0
            name = instructions.firstOrNullIfInstance<Name>() ?: Name.ANONYMOUS

            if (routes.isNotEmpty()) {
                navSegment = navSegment(AdaptiveStateVariableBinding(this, NAV_SEGMENT, "kotlin.String?"))
            } else {
                producers
                    ?.filter { it is NavSegmentProducer && it.binding.indexInTargetState == NAV_SEGMENT }
                    ?.forEach { removeProducer(it) }
            }
        }

        if (haveToPatch(dirtyMask, 1 shl NAV_SEGMENT)) {
            patchSegment()
        }

        // descendants are detached, so we should not propagate changes after the initial patch
        return isInit
    }

    fun patchSegment() {
        if (routes.isEmpty()) {
            setContent(defaultContent.build(this, flags) !!, null)
            return
        }

        val segment = navSegment
        if (segment != activeSegment) {
            val route = routes.firstOrNull { it.segment == segment }
            if (route == null) {
                setContent(defaultContent.build(this, flags) !!, segment)
            } else {
                route.detachFun(route) // calls `setContent`
            }
        }
    }

    fun setContent(origin: AdaptiveFragment, detachIndex: Int, segment: String?): AdaptiveFragment {
        if (trace) trace("setContent", "origin: $origin, detachIndex: $detachIndex segment: $segment")

        if (segment != navSegment) {
            // this is a navigation state change, we have to update the window URI accordingly
            navSegment = segment
            activeSegment = segment
            uiAdapter.navSupport.segmentChange(this, segment)
        }

        val fragment = origin.genBuild(this, detachIndex, DETACHED_MASK)
        checkNotNull(fragment) { "${origin}.genBuild(this, $detachIndex) returned with null" }

        // TODO should we set isDetached from genBuild?
        fragment.isDetached = true

        // only the origin fragment can patch the detached one, no one else has the necessary
        // information to do so
        origin.genPatchDescendant(fragment)

        fragment.create()

        return setContent(fragment, segment)
    }

    fun setContent(fragment: AdaptiveFragment, segment: String?): AdaptiveFragment {
        // genBuild calls `create`
        // `create` calls
        // - declaringFragment.genPatchDescendant
        // - genInternalPatch
        // so the state is correct at this point

        saveOrDisposeChildren()
        forwardHistory.clear()

        children.add(fragment)

        if (isMounted) fragment.mount()

        if (trace) trace("setContent", "fragment: $fragment")

        if (routes.isEmpty()) {
            return fragment
        }

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