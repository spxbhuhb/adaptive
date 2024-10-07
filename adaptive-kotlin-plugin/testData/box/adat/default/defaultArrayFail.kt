package `fun`.adaptive.adat

import `fun`.adaptive.utility.UUID

@Adat
class TestAdat(
    val someInt12: Int = 12,
    var someInt23: Int = 23,
    val someString: String
)

@Adat
class TestAdat2(
    val id: UUID<TestAdat2>,
    var someInt23 : Int,
    val someString: String
)

fun box(): String {

    try {
        val t1 = TestAdat.newInstance(arrayOf(null, null, null))
    } catch (ex : MissingParameterException) {
        if (ex.className != "fun.adaptive.adat.TestAdat") return "Fail: t1 adatClass ${ex.className}"
        if (ex.propertyName != "someString") return "Fail: t1 propertyName"
        if (ex.id != null) return "Fail: id"
    }

    val id = UUID<Any>()

    try {
        val t2 = TestAdat2.newInstance(arrayOf(id, null, "Hello"))
    } catch (ex : MissingParameterException) {
        if (ex.className != "fun.adaptive.adat.TestAdat2") return "Fail:  t2 adatClass ${ex.className}"
        if (ex.propertyName != "someInt23") return "Fail: t2 propertyName"
        if (ex.id != id) return "Fail: id"
    }

    return "OK"
}