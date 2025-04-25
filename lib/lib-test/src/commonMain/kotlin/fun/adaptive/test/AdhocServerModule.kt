package `fun`.adaptive.test

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace

class AdhocServerModule<WT : ServerWorkspace>(
    val implFun : () -> BackendFragmentImpl
) : AppModule<WT>() {

    override fun backendAdapterInit(adapter: BackendAdapter) {
        val impl = implFun()
        when (impl) {
            is WorkerImpl<*> -> workspace.workers += impl
            is ServiceImpl<*> -> workspace.services += impl
            else -> error("unknown implementation type: ${impl::class.simpleName}")
        }
    }
}