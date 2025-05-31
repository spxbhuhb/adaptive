package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace

class AdhocClientBackendModule<FW : AbstractWorkspace, BW : BackendWorkspace>(
    val implFun : () -> BackendFragmentImpl
) : AppModule<FW,BW>() {

    override fun backendAdapterInit(adapter: BackendAdapter) {
        val impl = implFun()
        when (impl) {
            is WorkerImpl<*> -> application.backendWorkspace.workers += impl
            is ServiceImpl<*> -> application.backendWorkspace.services += impl
            else -> error("unknown implementation type: ${impl::class.simpleName}")
        }
    }

}