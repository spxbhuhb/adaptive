package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: Int): Int
    suspend fun testValueNull(arg1: Int?): Int?
    suspend fun testArray(arg1: IntArray) : IntArray
    suspend fun testArrayNull(arg1: IntArray?) : IntArray?
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {
    override var serviceCallTransport: ServiceCallTransport? = null

    override suspend fun testValue(arg1: Int): Int = arg1
    override suspend fun testValueNull(arg1: Int?): Int? = arg1
    override suspend fun testArray(arg1: IntArray) : IntArray = arg1
    override suspend fun testArrayNull(arg1: IntArray?) : IntArray? = arg1
}

fun box(): String {
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestServiceImpl())

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