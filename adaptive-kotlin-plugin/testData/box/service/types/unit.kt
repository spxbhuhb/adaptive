package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.Service
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

interface TestService : Service {
    suspend fun testValue(arg1: Unit): Unit
    suspend fun testValueNull(arg1: Unit?): Unit?
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {
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