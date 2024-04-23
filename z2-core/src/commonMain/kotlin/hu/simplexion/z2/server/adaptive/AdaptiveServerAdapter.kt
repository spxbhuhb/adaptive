/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.server.adaptive

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Adapter for server applications.
 */
open class AdaptiveServerAdapter: AdaptiveAdapter<AdaptiveServerBridgeReceiver> {

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment<AdaptiveServerBridgeReceiver>

    override val rootBridge = AdaptiveServerPlaceholder()

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = false

    override fun createPlaceholder(): AdaptiveBridge<AdaptiveServerBridgeReceiver> {
        return AdaptiveServerPlaceholder()
    }

    override fun newId(): Long =
        nextId++

    companion object {
        init {
            AdaptiveAdapterRegistry.register(AdaptiveServerAdapterFactory)
        }
    }
}