/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.internal

import hu.simplexion.adaptive.foundation.AdaptiveFragment

class BoundSupportFunction(
    val declaringFragment: AdaptiveFragment<*>,
    val receivingFragment: AdaptiveFragment<*>,
    val supportFunctionIndex : Int
) {

    /**
     * Invokes the function in the closure of [receivingFragment]. As the function declaration
     * may be somewhere in anonymous components, the function execution may need variables from the
     * closure of those anonymous fragments.
     */
    fun invoke(vararg arguments: Any?): Any? {
        return declaringFragment.invoke(this, arguments)
    }

    suspend fun invokeSuspend(vararg arguments: Any?): Any? {
        return declaringFragment.invokeSuspend(this, arguments)
    }

    override fun toString() =
        "BoundSupportFunction(${declaringFragment.id}, ${receivingFragment.id}, $supportFunctionIndex)"
}