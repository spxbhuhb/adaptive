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
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) {
    companion object {
        val a = "A"
    }
}

fun hash(someInt: Int?, someBoolean: Boolean?, someIntListSet: Set<List<Int>>?): Int {
    var result = someInt ?: 0
    result = 31 * result + (someBoolean?.hashCode() ?: 0)
    result = 31 * result + (someIntListSet?.hashCode() ?: 0)
    return result
}

fun box(): String {
    val t1 = TestAdat()

    t1.someBoolean = true
    t1.setValue("someInt", 12)

    if (! t1.someBoolean) "Fail"

    if (! t1.equals(t1)) return "Fail: equals"
    if (t1.toString() != "TestAdat(someInt=12, someBoolean=true, someIntListSet=null)") return "Fail: toString ${t1.toString()}"
    if (t1.hashCode() != hash(12, true, null)) return "Fail: hashCode"

    if (WireFormatRegistry["hu.simplexion.adaptive.adat.TestAdat"] == null) return "Fail: not in WireFormatRegistry"

    return "OK"
}