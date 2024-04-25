package hu.simplexion.z2.services

import hu.simplexion.z2.services.*
import hu.simplexion.z2.services.transport.*
import hu.simplexion.z2.services.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

interface BasicTestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

interface TestService : Service {
    suspend fun testFun(arg1: Int, arg2: String): String

    class Consumer : TestService {

        override var fqName = "TestService"

        override var serviceCallTransport: ServiceCallTransport? = null

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

val testServiceConsumer = getService<BasicTestService>()

class BasicTestServiceImpl : BasicTestService, ServiceImpl<BasicTestServiceImpl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

fun box(): String {
    var response: String
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(BasicTestServiceImpl())
        println(defaultServiceCallTransport)
        response = testServiceConsumer.testFun(1, "hello")
    }
    return if (response.startsWith("i:1 s:hello BasicServiceContext(")) "OK" else "Fail (response=$response)"
}