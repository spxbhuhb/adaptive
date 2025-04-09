package `fun`.adaptive.adat

import `fun`.adaptive.adat.*

@Adat
class TestAdat(
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
)

fun box(): String {
    val a = TestAdat(12, false, setOf(listOf(23)))
    val b = a.copy()

    if (a === b) return "Fail: a === b"
    if (a.someInt != b.someInt) return "Fail: a.someInt != b.someInt"
    if (a.someBoolean != b.someBoolean) return "Fail: a.someBoolean != b.someBoolean"
    if (a.someIntListSet != b.someIntListSet) return "Fail: a.someIntListSet != b.someIntListSet"

    val c = a.copy(someInt = 34)
    if (c.someInt != 34) return "Fail: c.someInt != 34"
    if (a.someBoolean != c.someBoolean) return "Fail: a.someBoolean != c.someBoolean"
    if (a.someIntListSet != c.someIntListSet) return "Fail: a.someIntListSet != c.someIntListSet"

    return "OK"
}