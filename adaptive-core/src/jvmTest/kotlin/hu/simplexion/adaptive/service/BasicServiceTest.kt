/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.wireformat.WireFormatProvider
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class BasicServiceTest {

    @Test
    fun basic() {
        suspend fun test(provider: WireFormatProvider): String {
            val c = TestApi1.Consumer()
            c.serviceCallTransport = TestServiceTransport(TestService1(ServiceContext()), wireFormatProvider = provider)
            return c.testFun(1, "hello")
        }

        runBlocking {
            assertTrue(test(ProtoWireFormatProvider()).startsWith("i:1 s:hello ServiceContext("))
            assertTrue(test(JsonWireFormatProvider()).startsWith("i:1 s:hello ServiceContext("))
        }

    }

}