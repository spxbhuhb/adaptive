package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

interface TestService1 : Service {
    suspend fun testFun(arg1: Int, arg2: String): String
}

val testServiceConsumer = getService<TestService1>()

class TestService1Impl : TestService1, ServiceImpl<TestService1Impl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService1Impl())
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello BasicServiceContext(")) "OK" else "Fail (response=$response)"
}