/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.backend

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.backend.builtin.BackendService
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.sleep
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Adapter for backends (stores, services, workers).
 */
open class BackendAdapter(
    wait : Boolean = false
) : AdaptiveAdapter, ServiceImplFactory {

    override val fragmentFactory = BackendFragmentFactory

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val rootContainer
        get() = throw NotImplementedError()

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace : Array<out Regex> = emptyArray<Regex>()

    // TODO implement cache synchronization

    val serviceCache = mutableMapOf<String, BackendService>()

    val lock = getLock()

    var wait : Boolean = wait
        get() = lock.use { field }
        set(v) { lock.use { field = v } }

    override fun addActualRoot(fragment: AdaptiveFragment) {
        // there is no actual UI for backend fragments
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        // there is no actual UI for backend fragments
    }

    override fun newId(): Long =
        nextId ++

    open fun getLogger(name : String) = `fun`.adaptive.log.getLogger(name)

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