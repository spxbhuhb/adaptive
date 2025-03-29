package `fun`.adaptive.lib.util.testing

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto

class TestServiceTransport(
    dump: Boolean = false,
    wireFormatProvider: WireFormatProvider = Proto,
    name: String = "direct",
) : DirectServiceTransport(
    dump, wireFormatProvider, name
) {

    var contextFun : ((transport : TestServiceTransport) -> ServiceContext) ? = null

    var skipDisconnect = false

    override fun context(): ServiceContext {
        return contextFun?.invoke(this) ?: super.context()
    }

    override suspend fun disconnect() {
        if (skipDisconnect) return
        super.disconnect()
    }
}