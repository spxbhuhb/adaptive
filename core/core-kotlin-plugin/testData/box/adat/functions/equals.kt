package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.backend.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat(
    var someInt: Int
) {
    override fun equals(other: Any?): Boolean {
        return this.someInt == 12 && (other is TestAdat && other.someInt == 23)
    }
}

fun box(): String {
    val t1 = TestAdat(12)
    val t2 = TestAdat(23)

    if (t1 != t2) return "Fail: t1 != t2"

    return "OK"
}