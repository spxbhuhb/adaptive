/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.builtin.StringWireFormat
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

@ServiceApi
interface TestService1 {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService1, ServiceConsumer {

        override var serviceName = "hu.simplexion.adaptive.services.TestService1"

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

@ServiceApi
interface TestService2 {

    suspend fun testFun(arg1: Int, arg2: String): String

}

class TestService2Impl(
    override val serviceContext: ServiceContext
) : TestService2, ServiceImpl<TestService2Impl> {

    override var serviceCallTransport: ServiceCallTransport? = null

    override fun newInstance(serviceContext: ServiceContext): TestService2Impl {
        return TestService2Impl(serviceContext)
    }

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder<*>): ByteArray =
        when (funName) {
            "testFun;IS" -> wireFormatEncoder().rawInstance(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")), StringWireFormat)
            else -> throw IllegalStateException("unknown function: $funName")
        }.pack()

}

class BasicServiceTest1 {

    @Test
    fun basic() {
        suspend fun test(provider: WireFormatProvider): String {
            val c = TestService1.Consumer()
            c.serviceCallTransport = TestServiceTransport(TestService2Impl(ServiceContext()), wireFormatProvider = provider)
            return c.testFun(1, "hello")
        }

        runBlocking {
            assertTrue(test(ProtoWireFormatProvider()).startsWith("i:1 s:hello ServiceContext("))
            assertTrue(test(JsonWireFormatProvider()).startsWith("i:1 s:hello ServiceContext("))
        }

    }

}