package `fun`.adaptive.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ClientWorkspace

class BasicBrowserClientBuilder {

    val modules = mutableListOf<AppModule<ClientWorkspace>>()

    fun module(moduleFun: () -> AppModule<ClientWorkspace>) {
        modules += moduleFun()
    }

}