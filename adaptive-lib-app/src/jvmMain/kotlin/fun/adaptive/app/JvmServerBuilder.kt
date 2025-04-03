package `fun`.adaptive.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ServerWorkspace

class JvmServerBuilder {

    val modules = mutableListOf<AppModule<ServerWorkspace>>()

    fun module(moduleFun: () -> AppModule<ServerWorkspace>) {
        modules += moduleFun()
    }
}