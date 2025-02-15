package `fun`.adaptive.adat

import `fun`.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat(
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) {
    companion object : AdatCompanion<TestAdat>
}

fun hash(someInt: Int?, someBoolean: Boolean?, someIntListSet: Set<List<Int>>?): Int {
    var result = someInt ?: 0
    result = 31 * result + (someBoolean?.hashCode() ?: 0)
    result = 31 * result + (someIntListSet?.hashCode() ?: 0)
    return result
}

fun box(): String {
    val t1 = TestAdat(12, true, emptySet())

    if (! t1.someBoolean) "Fail"

    if (! t1.equals(t1)) return "Fail: equals"
    if (t1.toString() != "TestAdat(someInt=12, someBoolean=true, someIntListSet=[])") return "Fail: toString ${t1.toString()}"
    if (t1.hashCode() != hash(12, true, null)) return "Fail: hashCode"

    if (WireFormatRegistry["fun.adaptive.adat.TestAdat"] == null) return "Fail: not in WireFormatRegistry"

    return "OK"
}