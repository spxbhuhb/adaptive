package `fun`.adaptive.service

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: List<Int>): List<Int>
    suspend fun testValueNull(arg1: List<Int?>?): List<Int?>?
    suspend fun testValueList(arg1: List<List<Int>>): List<List<Int>>
    //suspend fun testArray(arg1: Array<List<Int>>) : Array<List<Int>>
}

val testServiceConsumer = getService<TestService>(TestServiceTransport(TestServiceImpl()))

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {

    override var serviceCallTransport: ServiceCallTransport
        get() = serviceContext.transport
        set(v) { TODO() }

    override suspend fun testValue(arg1: List<Int>): List<Int> = arg1
    override suspend fun testValueNull(arg1: List<Int?>?): List<Int?>? = arg1
    override suspend fun testValueList(arg1: List<List<Int>>): List<List<Int>> = arg1
    //override suspend fun testArray(arg1: Array<List<Int>>) : Array<List<Int>> = arg1
}

fun box(): String {
    runBlocking {

        val value = listOf(1, 2, 3)
        val valueList = listOf(listOf(1, 2), listOf(3, 4))
        //val arrayValue = arrayOf(listOf(1,2), listOf(3,4))

        if (value != testServiceConsumer.testValue(value)) return@runBlocking "Fail: value"
        if (null != testServiceConsumer.testValueNull(null)) return@runBlocking "Fail: null value"
        if (valueList != testServiceConsumer.testValueList(valueList)) return@runBlocking "Fail: testValueList"
        //if (! arrayValue.contentEquals(testServiceConsumer.testArray(arrayValue)) return@runBlocking "Fail: arrayValue"

        Unit
    }

    return "OK"
}