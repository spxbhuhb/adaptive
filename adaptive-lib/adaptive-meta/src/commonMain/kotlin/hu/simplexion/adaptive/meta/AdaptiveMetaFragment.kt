/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.meta

import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.manualImplementation

fun Adaptive.metaFragment(
    buildFunRegistry: AdaptiveMetaBuildFunRegistry<Any?>,
    patchFunRegistry: AdaptiveMetaPatchFunRegistry,
    metadata: AdaptiveMetaFragmentData
) {
    manualImplementation(buildFunRegistry, patchFunRegistry, metadata)
}

class AdaptiveMetaFragment<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 3) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(index: Int): T = state[index] as T

    val buildFunRegistry: AdaptiveMetaBuildFunRegistry<BT>
        get() = get(0)

    val patchFunRegistry: AdaptiveMetaPatchFunRegistry
        get() = get(1)

    val metadata: AdaptiveMetaFragmentData
        get() = get(2)

    val patchInstructions = mutableMapOf<Int, List<AdaptivePatchInstructionImpl>>()

    val fragments = mutableMapOf<Int, AdaptiveFragment<BT>>()

    override fun create() {
        if (trace) trace("before-Create")
        patch()
        metadata.keys.forEach { fragments[it] = genBuild(this, it) }
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