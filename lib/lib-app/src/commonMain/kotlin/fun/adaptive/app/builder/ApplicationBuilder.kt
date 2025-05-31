package `fun`.adaptive.app.builder

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto

class ApplicationBuilder<FW : AbstractWorkspace, BW : AbstractWorkspace> {

    var wireFormatProvider: WireFormatProvider = Proto

    @Deprecated("use service transport registry instead")
    var localTransport: Boolean = false

    val modules = mutableListOf<AppModule<FW, BW>>()

    fun module(moduleFun: () -> AppModule<FW, BW>) {
        modules += moduleFun()
    }

}