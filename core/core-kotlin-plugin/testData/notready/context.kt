package `fun`.adaptive.services.runtime.test.box.context

import `fun`.adaptive.services.Service
import `fun`.adaptive.services.getService
import `fun`.adaptive.services.ServiceContext
import `fun`.adaptive.services.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.services.defaultServiceImplFactory
import `fun`.adaptive.util.UUID

interface TestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

    suspend fun testFun(): UUID<TestService>

}

fun <T> ServiceContext.ensure(vararg roles: String, block: () -> T): T {
    return block()
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

    override suspend fun testFun() =
        serviceContext.ensure { UUID<TestService>() }
}

fun box(): String {
    defaultServiceImplFactory += TestServiceImpl()

    var response = runBlocking { testServiceConsumer.testFun(1, "hello") }

    if (! response.startsWith("i:1 s:hello")) return "Fail (response=$response)"

    val uuidResponse = runBlocking { testServiceConsumer.testFun() }

    return "OK"
}