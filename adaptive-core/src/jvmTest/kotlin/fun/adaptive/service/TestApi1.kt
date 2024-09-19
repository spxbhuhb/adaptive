package `fun`.adaptive.service

import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.builtin.StringWireFormat

@ServiceApi
interface TestApi1 {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestApi1, ServiceConsumer {

        override var serviceName = "fun.adaptive.services.TestApi1"

        override var serviceCallTransport: ServiceCallTransport
            get() = requireNotNull(serviceCallTransportOrNull)
            set(value) {
                serviceCallTransportOrNull = value
            }

        var serviceCallTransportOrNull: ServiceCallTransport? = null

        override suspend fun testFun(arg1: Int, arg2: String): String =
            wireFormatDecoder(
                callService(
                    "testFun;IS",
                    wireFormatEncoder()
                        .pseudoInstanceStart()
                        .int(1, "arg1", arg1)
                        .string(2, "arg2", arg2)
                        .pseudoInstanceEnd()
                )
            ).asInstance(StringWireFormat)
    }
}