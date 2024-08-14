package `fun`.adaptive.service

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService1 {
    suspend fun testFun(arg1: Int, arg2: String): String
}

val testServiceConsumer = getService<TestService1>()

class TestService1Impl : TestService1, ServiceImpl<TestService1Impl> {
    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService1Impl())
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello ServiceContext(")) "OK" else "Fail (response=$response)"
}