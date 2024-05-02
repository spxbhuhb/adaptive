/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.builtin.StringWireFormat
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

interface TestService1 : Service {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService1 {

        override var serviceName = "hu.simplexion.adaptive.services.TestService1"

        override var serviceCallTransport: ServiceCallTransport? = null

        override suspend fun testFun(arg1: Int, arg2: String): String =
            wireFormatDecoder(
                callService(
                    "testFun;IS",
                    wireFormatEncoder()
                        .int(1, "arg1", arg1)
                        .string(2, "arg2", arg2)
                )
            ).asInstance(StringWireFormat)
    }
}

interface TestService2 : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

val testServiceConsumer = TestService1.Consumer()

class TestService2Impl(
    override val serviceContext: ServiceContext
) : TestService2, ServiceImpl<TestService2Impl> {

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

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService2Impl(BasicServiceContext()))
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello BasicServiceContext(")) "OK" else "Fail (response=$response)"
}

class BasicServiceTest1 {

    @Test
    fun basicTest() {
        assertEquals("OK", box())
    }

}