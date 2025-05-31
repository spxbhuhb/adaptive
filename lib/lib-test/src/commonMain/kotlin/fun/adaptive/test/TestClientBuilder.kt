package `fun`.adaptive.test

import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace

class TestClientBuilder {

    val backendModules = mutableListOf<AppModule<AbstractWorkspace, BackendWorkspace>>()

    fun backendModule(moduleFun: () -> AppModule<AbstractWorkspace, BackendWorkspace>) {
        backendModules += moduleFun()
    }

    fun service(implFun: () -> BackendFragmentImpl) {
        backendModules += AdhocClientBackendModule(implFun)
    }

    fun worker(implFun: () -> BackendFragmentImpl) {
        backendModules += AdhocClientBackendModule(implFun)
    }

}