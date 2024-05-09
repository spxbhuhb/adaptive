/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.template

import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.adat.wireformat.toPropertyWireFormat
import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatDecoder

fun Adaptive.templateRealization(template: AdaptiveTemplateData) {
    manualImplementation(this)
}

class AdaptiveTemplateRealization<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 31) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(index: Int): T = state[index] as T

    val template: AdaptiveTemplateData
        get() = get(0)

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    override fun create() {
        if (trace) trace("before-Create")

        patch()

        template.variables.values.forEach {
            AdatPropertyMetaData(it.name, it.index, it.signature)
                .toPropertyWireFormat()
                .decode(JsonWireFormatDecoder(it.json.encodeToByteArray()), state)
        }

        template.fragments.values.forEach { tf ->
            adapter.fragmentImplRegistry.build(tf.impl, adapter, this, tf.index).also {
                it.create()
                fragments += it
            }
        }

        if (trace) trace("after-Create")
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("before-Mount", "bridge", bridge)

        for (fragment in fragments) {
            fragment.mount(bridge)
        }

        if (adapter.trace) trace("after-Mount", "bridge", bridge)
    }

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        throw UnsupportedOperationException()
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {
        val dirtyMask = getThisClosureDirtyMask()

        checkNotNull(template.fragments[fragment.index]) { "invalid index: $index in template: ${template.uuid}" }
            .mapping
            .forEach { mapping ->
                if (fragment.haveToPatch(dirtyMask, 1 shl mapping.sourceVariableIndex)) {
                    fragment.setStateVariable(mapping.targetVariableIndex, getThisClosureVariable(mapping.sourceVariableIndex))
                }
            }
    }

    override fun genPatchInternal() {

    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", "bridge", bridge)

        for (fragment in fragments) {
            fragment.unmount(bridge)
        }

        if (trace) trace("after-Unmount", "bridge", bridge)
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")

        for (f in fragments) {
            f.dispose()
        }

        if (trace) trace("after-Dispose")
    }

    override fun filter(result : MutableList<AdaptiveFragment<BT>>, filterFun : (it : AdaptiveFragment<BT>) -> Boolean) {
        if (filterFun(this)) {
            result += this
        }
        fragments.forEach {
            it.filter(result, filterFun)
        }
    }

}