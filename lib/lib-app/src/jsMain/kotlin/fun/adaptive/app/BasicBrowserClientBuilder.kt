package `fun`.adaptive.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ClientWorkspace

class BasicBrowserClientBuilder {

    val modules = mutableListOf<AppModule<ClientWorkspace>>()

    var localTransport : Boolean = false

    fun module(moduleFun: () -> AppModule<ClientWorkspace>) {
        modules += moduleFun()
    }

}