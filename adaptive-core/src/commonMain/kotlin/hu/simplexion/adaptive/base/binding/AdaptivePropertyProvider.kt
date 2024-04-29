/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base.binding

interface AdaptivePropertyProvider {

    fun addBinding(binding: AdaptiveStateVariableBinding<*>)

    fun removeBinding(binding: AdaptiveStateVariableBinding<*>)

    fun getValue(path : Array<String>) : Any?

    fun setValue(path : Array<String>, value : Any?, fromBinding: AdaptiveStateVariableBinding<*>)

}