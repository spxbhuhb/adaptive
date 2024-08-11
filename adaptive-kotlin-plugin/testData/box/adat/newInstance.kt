package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat(
    var someInt: Int,
    var someBoolean: Boolean
)

@Adat
class TestAdat2(
    var someInt: Int,
    var someBoolean: Boolean
) {
    companion object {
        val b = 23
    }
}

fun box(): String {
    val t1 = TestAdat.newInstance()

    if (t1.someBoolean) return "Fail: t1.someBoolean"
    if (t1.someInt != 0) return "Fail: t1.someInt"

    val t2 = TestAdat.newInstance(arrayOf(12, true))

    if (! t2.someBoolean) return "Fail: t2.someBoolean"
    if (t2.someInt != 12) return "Fail: t2.someInt"

    val t3 = TestAdat2.newInstance()

    if (t3.someBoolean) return "Fail: t3.someBoolean"
    if (t3.someInt != 0) return "Fail: t3.someInt"

    val t4 = TestAdat2.newInstance(arrayOf(12, true))

    if (! t4.someBoolean) return "Fail: t4.someBoolean"
    if (t4.someInt != 12) return "Fail: t4.someInt"

    return "OK"
}