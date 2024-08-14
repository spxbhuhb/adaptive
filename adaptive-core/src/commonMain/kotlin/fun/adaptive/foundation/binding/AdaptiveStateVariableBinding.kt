/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.binding

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.absolutePath
import `fun`.adaptive.adat.store.CopyStore
import `fun`.adaptive.foundation.AdaptiveFragment

class AdaptiveStateVariableBinding<VT>(
    val sourceFragment: AdaptiveFragment?,
    val indexInSourceState: Int,
    val indexInSourceClosure: Int,
    val targetFragment: AdaptiveFragment,
    val indexInTargetState: Int,
    val path: Array<String>?,
    val boundType : String,
    val adatCompanion: AdatCompanion<*>?,
) {

    constructor(targetFragment: AdaptiveFragment, indexInTargetState: Int, boundType: String) : this(
        sourceFragment = null,
        indexInSourceState = - 1,
        indexInSourceClosure = - 1,
        targetFragment,
        indexInTargetState,
        emptyArray<String>(),
        boundType,
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
        return "AdaptiveStateVariableBinding(${sourceFragment?.id}, $indexInSourceState, $indexInSourceState, ${targetFragment.id}, $indexInTargetState, ${path.contentToString()}, $boundType, ${adatCompanion?.wireFormatName})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AdaptiveStateVariableBinding<*>

        if (sourceFragment != other.sourceFragment) return false
        if (indexInSourceState != other.indexInSourceState) return false
        if (indexInSourceClosure != other.indexInSourceClosure) return false
        if (targetFragment != other.targetFragment) return false
        if (indexInTargetState != other.indexInTargetState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceFragment?.hashCode() ?: 0
        result = 31 * result + indexInSourceState
        result = 31 * result + indexInSourceClosure
        result = 31 * result + targetFragment.hashCode()
        result = 31 * result + indexInTargetState
        return result
    }

}