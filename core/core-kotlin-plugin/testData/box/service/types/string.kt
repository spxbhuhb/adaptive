package `fun`.adaptive.service

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: String): String
    suspend fun testValueNull(arg1: String?): String?
}

val testServiceConsumer = getService<TestService>(TestServiceTransport(TestServiceImpl()))

@ServiceProvider
class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl>() {

    override suspend fun testValue(arg1: String): String = arg1
    override suspend fun testValueNull(arg1: String?): String? = arg1
}

fun box(): String {
    runBlocking {

        val value = "hello"

        if (value != testServiceConsumer.testValue(value)) return@runBlocking "Fail: value"
        if (null != testServiceConsumer.testValueNull(null)) return@runBlocking "Fail: null value"

        Unit
    }

    return "OK"
}