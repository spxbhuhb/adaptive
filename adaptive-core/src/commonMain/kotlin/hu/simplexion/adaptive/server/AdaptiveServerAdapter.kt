/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.unsupported
import hu.simplexion.adaptive.server.builtin.ServerService
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.factory.ServiceImplFactory
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.sleep
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Adapter for server applications.
 */
open class AdaptiveServerAdapter(
    wait : Boolean = false
) : AdaptiveAdapter, ServiceImplFactory {

    override val fragmentFactory = ServerFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val rootContainer
        get() = throw NotImplementedError()

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace : Array<out Regex> = emptyArray<Regex>()

    // TODO implement cache synchronization

    val serviceCache = mutableMapOf<String, ServerService>()

    val lock = getLock()

    var wait : Boolean = wait
        get() = lock.use { field }
        set(v) { lock.use { field = v } }

    override fun addActualRoot(fragment: AdaptiveFragment) {
        // there is no actual UI for server fragments
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        // there is no actual UI for server fragments
    }

    override fun newId(): Long =
        nextId ++

    open fun getLogger(name : String) = hu.simplexion.adaptive.log.getLogger(name)

    override fun mounted() {
        while (wait) {
            sleep(1000)
        }
    }

    fun stop() {
        rootFragment.unmount()
    }

    override fun plusAssign(template: ServiceImpl<*>) {
        unsupported()
    }

    override fun minusAssign(template: ServiceImpl<*>) {
        unsupported()
    }

    override fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>? =
        serviceCache[serviceName]?.newInstance(context)

}