package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat(
    val someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
)

fun box(): String {
    val t1 = TestAdat()

    t1.someBoolean = true

    if (!t1.someBoolean) "Fail"

    if (!t1.equals(t1)) return "Fail: equals"
    if (t1.toString() != "TestAdat") return "Fail: toString"
    if (t1.hashCode() != arrayOf(null,true,null).contentHashCode()) return "Fail: hashCode"

    if (WireFormatRegistry["hu.simplexion.adaptive.adat.TestAdat"] == null) return "Fail: not in WireFormatRegistry"

    return "OK"
}