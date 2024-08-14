package `fun`.adaptive.service

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.defaultServiceCallTransport
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: Unit): Unit
    suspend fun testValueNull(arg1: Unit?): Unit?
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {
    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun testValue(arg1: Unit): Unit = arg1
    override suspend fun testValueNull(arg1: Unit?): Unit? = arg1
}

fun box(): String {
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestServiceImpl())

        val value = Unit

        if (value != testServiceConsumer.testValue(value)) return@runBlocking "Fail: value"
        if (null != testServiceConsumer.testValueNull(null)) return@runBlocking "Fail: null value"

        Unit
    }

    return "OK"
}