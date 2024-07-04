package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking

@ServiceApi
interface TestService {
    suspend fun testValue(arg1: List<Int>): List<Int>
    suspend fun testValueNull(arg1: List<Int?>?): List<Int?>?
    suspend fun testValueList(arg1: List<List<Int>>): List<List<Int>>
    //suspend fun testArray(arg1: Array<List<Int>>) : Array<List<Int>>
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {
    override suspend fun testValue(arg1: List<Int>): List<Int> = arg1
    override suspend fun testValueNull(arg1: List<Int?>?): List<Int?>? = arg1
    override suspend fun testValueList(arg1: List<List<Int>>): List<List<Int>> = arg1
    //override suspend fun testArray(arg1: Array<List<Int>>) : Array<List<Int>> = arg1
}

fun box(): String {
    runBlocking {
        defaultServiceCallTransport = TestServiceTransport(TestServiceImpl())

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