/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.binding

import hu.simplexion.adaptive.base.AdaptiveFragment

class AdaptiveStateVariableBinding<VT>(
    val sourceFragment: AdaptiveFragment<*>,
    val indexInSourceState: Int,
    val indexInSourceClosure: Int,
    val targetFragment: AdaptiveFragment<*>,
    val indexInTargetState: Int,
    val path: Array<String>?,
    val supportFunctionIndex: Int,
    val metadata: AdaptivePropertyMetadata,
) {

    @Suppress("UNCHECKED_CAST")
    val value: VT
        get() {
            val stateValue = sourceFragment.getThisClosureVariable(indexInSourceClosure)
            if (path == null) {
                return stateValue as VT
            } else {
                check(stateValue is AdaptivePropertyProvider)
                return stateValue.getValue(path) as VT
            }
        }

    fun setValue(value: Any?, setProviderValue : Boolean) {
        if (path == null) {
            sourceFragment.setStateVariable(indexInSourceState, value, this)
        } else {
            val provider = sourceFragment.getThisClosureVariable(indexInSourceClosure)

            check(provider is AdaptivePropertyProvider)

            if (setProviderValue) {
                provider.setValue(path, value, this)
            }

            // FIXME setting dirty masks and change propagation at bound value change
            // the line above behave very strange, find out why
            // sourceFragment.setDirty(indexInSourceState, true)
            targetFragment.setDirty(indexInTargetState, true)
        }
    }

    val propertyProvider: AdaptivePropertyProvider
        get() {
            check(path != null) { "this binding does not have a property provider" }
            return sourceFragment.getThisClosureVariable(indexInSourceClosure) as AdaptivePropertyProvider
        }

    override fun toString(): String {
        return "AdaptiveStateVariableBinding(${sourceFragment.id}, $indexInSourceState, $indexInSourceState, ${targetFragment.id}, ${indexInTargetState}, ${path.contentToString()}, $supportFunctionIndex, $metadata)"
    }

}