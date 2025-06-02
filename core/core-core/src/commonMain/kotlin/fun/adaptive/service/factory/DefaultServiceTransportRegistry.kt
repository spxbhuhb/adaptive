package `fun`.adaptive.service.factory

import `fun`.adaptive.service.testing.DirectServiceTransport
import `fun`.adaptive.wireformat.api.Json

object DefaultServiceTransportRegistry : ServiceTransportRegistry() {

    var trace = false

    init {
        add("direct") { url, wireFormatProvider, setupFun ->
            DirectServiceTransport(name = "client", wireFormatProvider = Json, setupFun = setupFun)
        }
    }
}