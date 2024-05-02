/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.log.logger
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Adapter for server applications.
 */
open class AdaptiveServerAdapter<BT> : AdaptiveAdapter<BT> {

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment<BT>

    override val rootBridge = AdaptiveServerPlaceholder<BT>()

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = false

    override fun createPlaceholder(): AdaptiveBridge<BT> {
        return AdaptiveServerPlaceholder()
    }

    override fun newId(): Long =
        nextId ++

    inline fun <reified T> single(): T {
        // TODO create fragment.filterIsInstance
        val fragment = rootFragment.single { it is AdaptiveServerFragment<*> && it.impl is T } as AdaptiveServerFragment<*>
        val implementation = fragment.impl as? T
        return checkNotNull(implementation) { "fragment $fragment implementation is not set" }
    }

    open fun getLogger(name : String) = logger(name)

    companion object {
        init {
            AdaptiveAdapterRegistry.register(AdaptiveServerAdapterFactory)
        }
    }

}