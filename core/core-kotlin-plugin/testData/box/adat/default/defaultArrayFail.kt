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
        return "Fail: t1 created"
    } catch (ex : NullPointerException) {

    }

    val id = UUID<Any>()

    try {
        val t2 = TestAdat2.newInstance(arrayOf(id, null, "Hello"))
        return "Fail: t2 created"
    } catch (ex : NullPointerException) {

    }

    return "OK"
}