package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: String): String
    suspend fun testValueNull(arg1: String?): String?
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {
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