package `fun`.adaptive.app.builder

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto

class ApplicationBuilder<WT : AbstractWorkspace> {

    var wireFormatProvider : WireFormatProvider = Proto

    var localTransport : Boolean = false

    val modules = mutableListOf<AppModule<WT>>()

    fun module(moduleFun: () -> AppModule<WT>) {
        modules += moduleFun()
    }

}