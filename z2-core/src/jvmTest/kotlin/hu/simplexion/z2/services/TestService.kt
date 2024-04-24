package hu.simplexion.z2.services

import hu.simplexion.z2.services.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

interface BasicTestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

val testServiceConsumer = getService<BasicTestService>()

class BasicTestServiceImpl : TestService, ServiceImpl<BasicTestServiceImpl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(BasicTestServiceImpl())
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello BasicServiceContext(")) "OK" else "Fail (response=$response)"
}