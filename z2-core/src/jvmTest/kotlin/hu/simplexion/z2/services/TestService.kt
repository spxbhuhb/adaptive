package hu.simplexion.z2.services

import hu.simplexion.z2.services.testing.TestServiceTransport
import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.wireformat.WireFormatDecoder
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

interface TestService1 : Service {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService1 {

        override var fqName = "hu.simplexion.z2.services.TestService1"

        override var serviceCallTransport: ServiceCallTransport? = null

        override suspend fun testFun(arg1: Int, arg2: String): String =
            wireFormatStandalone.decodeString(
                callService(
                    "testFun;IS",
                    wireFormatEncoder
                        .int(1, "arg1", arg1)
                        .string(2, "arg2", arg2)
                )
            )

    }
}

interface TestService2 : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

// val testServiceConsumer = getService<TestService2>()
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
            "testFun;IS" -> wireFormatStandalone.encodeString(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")))
            else -> throw IllegalStateException("unknown function: $funName")
        }

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