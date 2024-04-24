package foo.bar

import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.serialization.protobuf.ProtoOneString
import hu.simplexion.z2.services.Service
import hu.simplexion.z2.services.defaultServiceCallTransport
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.getService
import kotlinx.coroutines.runBlocking
import hu.simplexion.z2.services.defaultServiceImplFactory

interface TestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

val test = getService<TestService>()
val test2 = getService<TestService>().also { it.serviceName = "manual" }

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

class TestServiceImpl2 : TestService, ServiceImpl<TestServiceImpl2> {

    override var serviceName = "manual"

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

}

fun box(): String {
    var name = test.serviceName
    if (name != "foo.bar.TestService") return "Fail: Test.serviceName=$name"

    name = TestServiceImpl().serviceName
    if (name != "foo.bar.TestService") return "Fail TestServiceImpl().serviceName=$name"

    name = test2.serviceName
    if (name != "manual") return "Fail: Test2.serviceName=$name"

    name = TestServiceImpl2().serviceName
    if (name != "manual") return "Fail: TestServiceImpl2().serviceName=$name"

    return "OK"
}