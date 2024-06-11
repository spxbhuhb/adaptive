/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory

class AdaptiveAnonymous private constructor(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int,
    stateSize: Int,
    val factory: BoundFragmentFactory
) : AdaptiveFragment(adapter, parent, index, - 1, stateSize) {

    constructor(
        parent: AdaptiveFragment,
        index: Int,
        stateSize: Int,
        factory: BoundFragmentFactory
    ) : this(parent.adapter, parent, index, stateSize, factory)

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = extendWith(this, factory.declaringFragment) //.also { println(it.dump()) }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        factory.declaringFragment.genPatchDescendant(fragment)
    }

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return factory.build(this)
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