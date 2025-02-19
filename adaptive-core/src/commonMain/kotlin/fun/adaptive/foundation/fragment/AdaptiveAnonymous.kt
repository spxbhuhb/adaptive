/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.utility.debug

/**
 * Anonymous has two different modes: "reference" and "lambda".
 *
 * Reference mode is used in cases when a function reference is used in rendering,
 * lambda mode is used when a lambda function is used in rendering.
 */
class AdaptiveAnonymous private constructor(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int,
    stateSize: Int,
    val factory: BoundFragmentFactory
) : AdaptiveFragment(adapter, parent, index, stateSize) {

    constructor(
        parent: AdaptiveFragment,
        index: Int,
        stateSize: Int,
        factory: BoundFragmentFactory
    ) : this(parent.adapter, parent, index, stateSize, factory)

    val reference: Boolean = (factory.functionReference != null)

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure =
        if (reference) {
        AdaptiveClosure(arrayOf(this), state.size)
    } else {
        extendWith(this, factory.declaringFragment)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        if (reference) {
            state.forEachIndexed { index, value ->
                fragment.setStateVariable(index, value)
            }
        } else {
            factory.declaringFragment.genPatchDescendant(fragment)
        }
    }

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        if (reference) {
            factory.functionReference !!(this, declarationIndex)?.also { it.create() }
        } else {
            factory.declaringFragment.genBuild(this, factory.declarationIndex, flags)
        }

    override fun genPatchInternal(): Boolean = true

    /**
     * Finds the first parent with `thisClosure` owned by [declaringComponent]. Then extends that closure with
     * the component and returns with the extended closure.
     *
     * Anonymous components use this function to find their declaring closure and extend it with themselves.
     */
    fun extendWith(component: AdaptiveFragment, declaringComponent: AdaptiveFragment): AdaptiveClosure {
        var ancestor = component.parent

        while (ancestor != null && ancestor.thisClosure.owner !== declaringComponent) {
            ancestor = ancestor.parent
        }

        checkNotNull(ancestor) { "couldn't find declaring component for closure extension" }

        val declaringClosure = ancestor.thisClosure

        return AdaptiveClosure(
            declaringClosure.fragments + component,
            declaringClosure.closureSize + component.state.size
        )
    }

    companion object {
        /**
         * Make an anonymous fragment with a new adapter. This function can be used
         * to build a subtree with a different adapter than the current one.
         */
        fun switchAdapter(
            adapter: AdaptiveAdapter,
            parent: AdaptiveFragment,
            index: Int,
            stateSize: Int,
            factory: BoundFragmentFactory
        ) = AdaptiveAnonymous(adapter, parent, index, stateSize, factory)
    }
}