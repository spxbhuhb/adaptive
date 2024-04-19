package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.serialization.A
import kotlin.test.Test

class DumpTest {

    @Test
    fun testDump() {
        ProtoMessageBuilder()
            .int(1, "i", 12)
            .string(2, "s", "Hello")
            .instance(3, "a", A, A(false, 123, "World", mutableListOf(12)))
            .pack()
            .dumpProto()
    }
}