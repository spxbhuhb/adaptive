/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.engine

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.manualImplementation
import hu.simplexion.adaptive.engine.model.AdaptiveEngineOperation
import hu.simplexion.adaptive.engine.model.AddFragment



fun Adaptive.engine(
    operations: List<AdaptiveEngineOperation>
) {
    manualImplementation(operations)
}

class AdaptiveEngine<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 31) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(index: Int): T = state[index] as T

    val operations: List<AdaptiveEngineOperation>
        get() = get(0)

    val fragments = mutableMapOf<Int, AdaptiveFragment<BT>>()

    override fun create() {
        if (trace) trace("before-Create")

        patch()

        operations.forEach {
            if (it is AddFragment) adapter.fragmentImplRegistry.build(it.impl, adapter, this, it.index)
        }

        if (trace) trace("after-Create")
    }

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {

        val data = metadata[declarationIndex] ?: invalidIndex(declarationIndex)
        val buildFun = buildFunRegistry[data.impl] ?: missingImpl(data.impl)

        val fragment = buildFun.invoke(adapter, parent, declarationIndex)

        patchInstructions[declarationIndex] = data.patchInstructions.map {
            (patchFunRegistry[it.impl] ?: missingImpl(data.impl)).invoke(it.data)
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {
        (patchInstructions[fragment.index] ?: invalidIndex(fragment.index)).forEach {
            it.patch(fragment)
        }
    }

    override fun genPatchInternal() {

    }

    fun missingImpl(impl: String): Nothing {
        throw IllegalStateException("missing implementation: $impl")
    }
}