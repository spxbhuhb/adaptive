package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*

@Adat
class TestAdat(
    val someInt12: Int = 12,
    var someInt23: Int = 23,
    val someString: String = "Hello"
)

fun box(): String {
    val t1 = TestAdat()

    if (t1.someInt12 != 12) "Fail: someInt12 != ${t1.someInt12}"
    if (t1.getValue("someInt12") != 12) "Fail: getValue(\"someInt12\") != ${t1.someInt12}"

    if (t1.someInt23 != 23) "Fail: someInt23 != ${t1.someInt23}"
    if (t1.someString != "Hello") "Fail: someInt23 != ${t1.someString}"

    return "OK"
}