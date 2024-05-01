package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.Service
import hu.simplexion.adaptive.service.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

interface TestService : Service {
    suspend fun testValue(arg1: String): String
    suspend fun testValueNull(arg1: String?): String?
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl,Any> {
    override suspend fun testValue(arg1: String): String = arg1
    override suspend fun testValueNull(arg1: String?): String? = arg1
}

fun box(): String {
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestServiceImpl())

        val value = "hello"

        if (value != testServiceConsumer.testValue(value)) return@runBlocking "Fail: value"
        if (null != testServiceConsumer.testValueNull(null)) return@runBlocking "Fail: null value"

        Unit
    }

    return "OK"
}