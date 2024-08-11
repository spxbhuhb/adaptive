package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.builtin.StringWireFormat

@ServiceApi
interface TestApi1 {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestApi1, ServiceConsumer {

        override var serviceName = "hu.simplexion.adaptive.services.TestApi1"

        override var serviceCallTransport: ServiceCallTransport? = null

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