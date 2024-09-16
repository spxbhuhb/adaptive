package `fun`.adaptive.service

import `fun`.adaptive.adat.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.runBlocking

abstract class AA : AdatClass {
    abstract val i: Int
}

@Adat
class A(
    override val i: Int
) : AA()

@ServiceApi
interface TestService1 {
    suspend fun testFun(a: AA): AA
}

val testServiceConsumer = getService<TestService1>()

class TestService1Impl : TestService1, ServiceImpl<TestService1Impl> {

    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun testFun(a: AA): AA = A(a.i + 1)

}

fun box(): String {
    var response: AA
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestService1Impl())
        response = testServiceConsumer.testFun(A(12))
    }
    return if (response.i == 13) "OK" else "Fail (response=$response)"
}