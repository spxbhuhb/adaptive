/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlin.time.measureTime

open class AdaptiveMeasureFragmentTime(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0, 2) {

    val content: BoundFragmentFactory
        get() = state[state.size - 1].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous(this, declarationIndex, 0, content).apply { create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

    override fun create() {
        val measureTime = measureTime { super.create() }
        adapter.log(this, "create duration", measureTime.toString())
    }

    override fun mount() {
        val measureTime = measureTime { super.mount() }
        adapter.log(this, "mount duration", measureTime.toString())
    }

    override fun unmount() {
        val measureTime = measureTime { super.unmount() }
        adapter.log(this, "unmount duration", measureTime.toString())
    }

    override fun dispose() {
        val measureTime = measureTime { super.dispose() }
        adapter.log(this, "mount dispose", measureTime.toString())
    }

}