/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service

import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class BasicServiceTest {

    @Test
    fun basic() {
        suspend fun test(provider: WireFormatProvider): String {
            val c = TestApi1.Consumer()
            c.serviceCallTransport = TestServiceTransport(TestService1(ServiceContext(TestServiceTransport())), wireFormatProvider = provider)
            return c.testFun(1, "hello")
        }

        runBlocking {
            assertTrue(test(Proto).startsWith("i:1 s:hello ServiceContext("))
            assertTrue(test(Json).startsWith("i:1 s:hello ServiceContext("))
        }

    }

}