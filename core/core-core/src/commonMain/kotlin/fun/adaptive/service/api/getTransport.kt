package `fun`.adaptive.service.api

import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.service.factory.DefaultServiceTransportRegistry
import `fun`.adaptive.service.factory.ServiceTransportRegistry
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto

/**
 * Gets a [service transport](def://) based on the schema in [url].
 */
fun getTransport(
    url : String,
    wireFormatProvider: WireFormatProvider = if (GlobalRuntimeContext.devMode) Json else Proto,
    registry : ServiceTransportRegistry = DefaultServiceTransportRegistry,
    setupFun : suspend (ServiceCallTransport) -> Unit = {  }
) : ServiceCallTransport {
    return registry.newInstance(url, wireFormatProvider, setupFun)
}