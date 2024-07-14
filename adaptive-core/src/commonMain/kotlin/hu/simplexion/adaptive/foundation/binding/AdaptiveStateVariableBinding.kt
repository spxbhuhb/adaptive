/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.binding

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.absolutePath
import hu.simplexion.adaptive.adat.store.CopyStore
import hu.simplexion.adaptive.foundation.AdaptiveFragment

class AdaptiveStateVariableBinding<VT>(
    val sourceFragment: AdaptiveFragment?,
    val indexInSourceState: Int,
    val indexInSourceClosure: Int,
    val targetFragment: AdaptiveFragment,
    val indexInTargetState: Int,
    val path: Array<String>?,
    val metadata: AdaptivePropertyMetadata?,
) {

    constructor(targetFragment: AdaptiveFragment, indexInTargetState: Int) : this(
        sourceFragment = null,
        indexInSourceState = - 1,
        indexInSourceClosure = - 1,
        targetFragment,
        indexInTargetState,
        emptyArray<String>(),
        null
    )

    @Suppress("UNCHECKED_CAST")
    val value: VT
        get() {
            checkNotNull(sourceFragment)
            val stateValue = sourceFragment.getThisClosureVariable(indexInSourceClosure)
            if (path == null) {
                return stateValue as VT
            } else {
                check(stateValue is AdaptivePropertyProvider)
                return stateValue.getValue(path) as VT
            }
        }

    fun setValue(value: Any?, setProviderValue: Boolean) {
        checkNotNull(sourceFragment)
        if (path == null) {
            sourceFragment.setStateVariable(indexInSourceState, value, this)
        } else {
            val provider = sourceFragment.getThisClosureVariable(indexInSourceClosure)

            when {
                provider is AdatClass<*> && provider.adatContext?.store is CopyStore<*> -> {
                    val absolutePath = provider.absolutePath() + path
                    (provider.adatContext?.store as CopyStore<*>).setProperty(absolutePath, value)
                }

                provider is AdaptivePropertyProvider -> {
                    if (setProviderValue) {
                        provider.setValue(path, value)
                    }

                    // FIXME setting dirty masks and change propagation at bound value change
                    // the line above behave very strange, find out why
                    // sourceFragment.setDirty(indexInSourceState, true)
                    targetFragment.setDirty(indexInTargetState, true)
                }

                else -> throw UnsupportedOperationException()
            }
        }
    }

    val propertyProvider: AdaptivePropertyProvider
        get() {
            checkNotNull(sourceFragment)
            check(path != null) { "this binding does not have a property provider" }
            return sourceFragment.getThisClosureVariable(indexInSourceClosure) as AdaptivePropertyProvider
        }

    override fun toString(): String {
        return "AdaptiveStateVariableBinding(${sourceFragment?.id}, $indexInSourceState, $indexInSourceState, ${targetFragment.id}, ${indexInTargetState}, ${path.contentToString()}, $metadata)"
    }

}