package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*

@Adat
class TestAdat(
    val someInt12: Int = 12,
    var someInt23: Int = 23,
    val someString: String = "Hello"
)

fun box(): String {
    val t1 = TestAdat()

    if (t1.someInt12 != 12) return "Fail: someInt12 != ${t1.someInt12}"
    if (t1.getValue("someInt12") != 12) return "Fail: getValue(\"someInt12\") != ${t1.someInt12}"

    if (t1.someInt23 != 23) return "Fail: someInt23 != ${t1.someInt23}"
    if (t1.someString != "Hello") return "Fail: someInt23 != ${t1.someString}"

    return "OK"
}