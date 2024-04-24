package hu.simplexion.z2.services

import hu.simplexion.z2.services.testing.TestServiceTransport
import hu.simplexion.z2.services.transport.LocalServiceCallTransport
import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.wireformat.WireFormatDecoder
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicServiceTest {

    fun box(): String {
        var response: String
        runBlocking {
            response = TestService.TestServiceConsumer().testFun(1, "hello")
        }
        return if (response.startsWith("i:1 s:hello BasicServiceContext(uuid=")) "OK" else "Fail (response=$response)"
    }

    @Test
    fun basicTest() {
        assertEquals("OK", box())
    }

}

interface TestService : Service {
    suspend fun testFun(arg1: Int, arg2: String): String

    class TestServiceConsumer : TestService {

        override var fqName = "TestService"

        override var serviceCallTransport: ServiceCallTransport? =
            // defaultServiceCallTransport in actual code
            TestServiceTransport(TestServiceImpl(BasicServiceContext()))

        override suspend fun testFun(arg1: Int, arg2: String): String =
            wireFormatStandalone.decodeString(
                callService(
                    "testFun",
                    wireFormatEncoder
                        .int(1, "arg1", arg1)
                        .string(2, "arg2", arg2)
                )
            )

    }
}

class TestServiceImpl(override val serviceContext: ServiceContext) : TestService, ServiceImpl<TestServiceImpl> {

    override var fqName = "TestService"

    override var serviceCallTransport: ServiceCallTransport? =
        // defaultServiceCallTransport in actual code
        LocalServiceCallTransport()

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder): ByteArray =
        when (funName) {
            "testFun" -> wireFormatStandalone.encodeString(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")))
            else -> throw IllegalStateException("unknown function: $funName")
        }

    override fun newInstance(serviceContext: ServiceContext): TestServiceImpl {
        return TestServiceImpl(serviceContext)
    }

    override suspend fun testFun(arg1: Int, arg2: String): String {
        return "i:$arg1 s:$arg2 $serviceContext"
    }

}