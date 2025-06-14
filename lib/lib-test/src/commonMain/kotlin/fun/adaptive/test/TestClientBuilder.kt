package `fun`.adaptive.test

import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace

class TestClientBuilder {

    val modules = mutableListOf<AppModule<AbstractWorkspace, BackendWorkspace>>()

    fun module(moduleFun: () -> AppModule<AbstractWorkspace, BackendWorkspace>) {
        modules += moduleFun()
    }

    fun service(implFun: () -> BackendFragmentImpl) {
        modules += AdhocClientBackendModule(implFun)
    }

    fun worker(implFun: () -> BackendFragmentImpl) {
        modules += AdhocClientBackendModule(implFun)
    }

}