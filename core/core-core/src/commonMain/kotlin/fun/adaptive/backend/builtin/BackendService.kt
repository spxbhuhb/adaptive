/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.backend
import `fun`.adaptive.service.ServiceContext

@AdaptiveExpect(backend)
fun service(vararg instructions: AdaptiveInstruction, impl: () -> BackendFragmentImpl): AdaptiveFragment {
    manualImplementation(instructions, impl)
}

@AdaptiveActual
class BackendService(
    adapter: BackendAdapter,
    parent: AdaptiveFragment,
    index: Int
) : BackendFragment(adapter, parent, index) {

    val serviceImpl : ServiceImpl<*>?
        get() = impl as ServiceImpl<*>?

    override fun mount() {
        if (trace) trace("before-Mount")

        serviceImpl?.let {
            it.mount()
            backendAdapter.addBackendService(this)
        }

        if (trace) trace("after-Mount")
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        checkNotNull(serviceImpl) { "inconsistent backend state innerUnmount with a null implementation" }
            .let {
                backendAdapter.removeBackendService(it.serviceName)
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