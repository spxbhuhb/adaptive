/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package foo.bar

import hu.simplexion.adaptive.services.Service
import hu.simplexion.adaptive.services.ServiceImpl
import hu.simplexion.adaptive.services.getService

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