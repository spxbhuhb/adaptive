package `fun`.adaptive.service

import `fun`.adaptive.adat.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@Adat
class A<T>(
    val i: Int
)

@ServiceApi
interface TestService1 {
    suspend fun testFun(): A<Any>
}

val testServiceConsumer = getService<TestService1>()

class TestService1Impl : TestService1, ServiceImpl<TestService1Impl> {
    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun testFun() : A<Any> = A(12)

}

fun box(): String {
    var response: A<Any>
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService1Impl())
        response = testServiceConsumer.testFun()
    }
    return if (response.i == 12) "OK" else "Fail (response=$response)"
}