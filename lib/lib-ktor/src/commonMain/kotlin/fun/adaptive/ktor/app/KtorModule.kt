package `fun`.adaptive.ktor.app

import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.service.factory.DefaultServiceTransportRegistry
import `fun`.adaptive.wireformat.WireFormatRegistry

open class KtorModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + AccessDenied
        DefaultServiceTransportRegistry["https"] = { url, wireFormatProvider, setupFun -> webSocketTransport(url, wireFormatProvider, setupFun) }
        DefaultServiceTransportRegistry["http"] = { url, wireFormatProvider, setupFun -> webSocketTransport(url, wireFormatProvider, setupFun) }
    }

}