package `fun`.adaptive.app

import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto

class BasicBrowserClientBuilder {

    val modules = mutableListOf<AppModule<ClientWorkspace>>()

    var wireFormatProvider : WireFormatProvider = Proto
    var localTransport : Boolean = false

    fun module(moduleFun: () -> AppModule<ClientWorkspace>) {
        modules += moduleFun()
    }

}