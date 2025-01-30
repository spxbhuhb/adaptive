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
    var someInt: Int,
    val someWithDefault : Int = 2
) {

    constructor() : this(12, 23)

    val i1 = someInt + 1
    var i2 = 3
    val i4
        get() = "Hello"
}

fun box(): String {
    val t1 = TestAdat(12)

    if (! t1.equals(t1)) return "Fail: equals"
    if (t1.toString() != "TestAdat(someInt=12, someWithDefault=2)") return "Fail: toString ${t1.toString()}"

    if (t1.i1 != 13) return "Fail: t1.i1 ${t1.i1}"

    val td = TestAdat.decodeFromJson(t1.encodeToJson())
    if (t1.i1 != td.i1) return "Fail: ${t1.i1} != ${td.i1}"

    val t2 = TestAdat(12)
    t2.i2 = 15

    if (t1 != t2) return "Fail: t1 != t2"

    return "OK"
}