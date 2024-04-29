/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

object AdaptiveAdapterRegistry {

    private val factories = mutableListOf<AdaptiveAdapterFactory>()

    fun register(factory: AdaptiveAdapterFactory) {
        factories += factory
    }

    @Suppress("unused") // used by generated code
    fun adapterFor(vararg args: Any?): AdaptiveAdapter<*> {
        for (factory in factories) {
            factory.accept(*args)?.let {
                return it
            }
        }
        throw NotImplementedError("no adapter accepted the specified parameters")
    }

}