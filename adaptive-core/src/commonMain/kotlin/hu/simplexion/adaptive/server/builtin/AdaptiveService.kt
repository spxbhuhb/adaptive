/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory

@Adaptive
fun service(impl: () -> ServerFragmentImpl<*>) {
    manualImplementation(impl)
}

class AdaptiveService<BT>(
    adapter: AdaptiveServerAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int
) : AdaptiveServerFragment<BT>(adapter, parent, index) {

    val serviceImpl : ServiceImpl<*>?
        get() = impl as ServiceImpl<*>?

    override fun innerMount(bridge: AdaptiveBridge<BT>) {
        serviceImpl?.let {
            it.mount()
            serverAdapter.serviceCache[it.serviceName] = this
        }
    }

    override fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        checkNotNull(serviceImpl) { "inconsistent server state innerUnmount with a null implementation" }
            .let {
                serverAdapter.serviceCache.remove(it.serviceName)
            }
        impl = null
    }

    fun newInstance(context : ServiceContext) : ServiceImpl<*> {
        return serviceImpl!!.newInstance(context).also {
            @Suppress("UNCHECKED_CAST")
            it.fragment = this as AdaptiveServerFragment<Any>
        }
    }

}