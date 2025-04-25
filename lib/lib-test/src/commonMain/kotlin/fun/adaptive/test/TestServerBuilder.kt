package `fun`.adaptive.test

import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace

class TestServerBuilder {

    val modules = mutableListOf<AppModule<ServerWorkspace>>()

    fun module(moduleFun: () -> AppModule<ServerWorkspace>) {
        modules += moduleFun()
    }

    fun adhoc(implFun: () -> BackendFragmentImpl) {
        modules += AdhocServerModule(implFun)
    }
}