/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.server.builtin

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.server.AdaptiveServerAdapter
import `fun`.adaptive.server.AdaptiveServerFragment
import `fun`.adaptive.server.server
import `fun`.adaptive.service.ServiceContext

@AdaptiveExpect(server)
fun service(vararg instructions: AdaptiveInstruction, impl: () -> ServerFragmentImpl): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class ServerService(
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