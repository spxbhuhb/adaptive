/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base

interface AdaptiveTransformInterface {

    fun getThisClosureVariable(variableIndex : Int) : Any?

    fun setStateVariable(index: Int, value: Any?)

}