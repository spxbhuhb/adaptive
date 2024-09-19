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
    suspend fun testValue(arg1: Int): Int
    suspend fun testValueNull(arg1: Int?): Int?
    suspend fun testArray(arg1: IntArray) : IntArray
    suspend fun testArrayNull(arg1: IntArray?) : IntArray?
}

val testServiceConsumer = getService<TestService>(TestServiceTransport(TestServiceImpl()))

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {

    override var serviceCallTransport: ServiceCallTransport?
        get() = serviceContext.transport
        set(v) { TODO() }

    override suspend fun testValue(arg1: Int): Int = arg1
    override suspend fun testValueNull(arg1: Int?): Int? = arg1
    override suspend fun testArray(arg1: IntArray) : IntArray = arg1
    override suspend fun testArrayNull(arg1: IntArray?) : IntArray? = arg1
}

fun box(): String {
    runBlocking {
        val value = 123
        val valueArray = IntArray(3) { it }

        if (value != testServiceConsumer.testValue(value)) return@runBlocking "Fail: value"
        if (null != testServiceConsumer.testValueNull(null)) return@runBlocking "Fail: null value"
        if (! valueArray.contentEquals(testServiceConsumer.testArray(valueArray))) return@runBlocking "Fail: valueArray"
        if (null != testServiceConsumer.testArrayNull(null)) return@runBlocking "Fail: null valueArray"

        Unit
    }

    return "OK"
}