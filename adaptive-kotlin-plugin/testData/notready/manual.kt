package hu.simplexion.adaptive.services

import hu.simplexion.adaptive.services.*
import hu.simplexion.adaptive.services.transport.*
import hu.simplexion.adaptive.services.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

interface TestService1 : Service {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService1 {

        override var fqName = "hu.simplexion.adaptive.services.TestService1"

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

val testServiceConsumer = TestService1.Consumer()

class TestService1Impl : TestService1, ServiceImpl<TestService1Impl> {

    override val serviceContext = BasicServiceContext()

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

    override suspend fun dispatch(funName: String, payload: WireFormatDecoder): ByteArray =
        when (funName) {
            "testFun;IS" -> wireFormatStandalone.encodeString(testFun(payload.int(1, "arg1"), payload.string(2, "arg2")))
            else -> TODO()
        }

}

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService1Impl())
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello BasicServiceContext(")) "OK" else "Fail (response=$response)"
}