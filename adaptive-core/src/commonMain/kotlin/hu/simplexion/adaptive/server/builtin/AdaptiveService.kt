/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.builtin

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.defaultServiceImplFactory

@Adaptive
fun service(impl: () -> ServerFragmentImpl) {
    manualImplementation(impl)
}

class AdaptiveService(
    adapter: AdaptiveServerAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveServerFragment(adapter, parent, index) {

    val serviceImpl : ServiceImpl<*>?
        get() = impl as ServiceImpl<*>?

    override fun mount() {
        if (trace) trace("before-Mount")

        serviceImpl?.let {
            it.mount()
            serverAdapter.serviceCache[it.serviceName] = this
        }

        if (trace) trace("after-Mount")
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        checkNotNull(serviceImpl) { "inconsistent server state innerUnmount with a null implementation" }
            .let {
                serverAdapter.serviceCache.remove(it.serviceName)
            }
        impl = null

        if (trace) trace("after-Unmount")
    }

    fun newInstance(context : ServiceContext) : ServiceImpl<*> {
        return serviceImpl!!.newInstance(context).also {
            it.fragment = this
        }
    }

}