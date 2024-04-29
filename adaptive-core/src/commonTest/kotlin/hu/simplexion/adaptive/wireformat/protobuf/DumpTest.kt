/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.wireformat.A
import kotlin.test.Test

class DumpTest {

    @Test
    fun testDump() {
        ProtoWireFormatEncoder()
            .int(1, "i", 12)
            .string(2, "s", "Hello")
            .instance(3, "a", A(false, 123, "World", mutableListOf(12)), A)
            .pack()
            .dumpProto()
    }
}