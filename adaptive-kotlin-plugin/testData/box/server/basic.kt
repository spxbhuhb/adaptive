package `fun`.adaptive.service

import `fun`.adaptive.server.*
import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder

//interface TestService : Service {
//    suspend fun testFun(arg1: Int, arg2: String): String
//}
//
//class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl,Any> {
//
//    override suspend fun testFun(arg1: Int, arg2: String) =
//        "i:$arg1 s:$arg2 $serviceContext"
//
//}

fun box(): String {
//    val impl = TestServiceImpl()
//    if (impl.serverAdapter != null) return "Fail: not null"
//
//    val adapter = AdaptiveServerAdapter<Any>()
//    impl.serverAdapter = adapter
//    if (impl.serverAdapter != adapter) "Fail: not the adapter"

    return "OK"
}