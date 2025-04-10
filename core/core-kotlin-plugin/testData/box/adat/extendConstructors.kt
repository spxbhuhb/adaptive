package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.model.ReturnException

@Adat
class TestAdat(
    val i: Int
) : ReturnException()

fun box(): String {
    TestAdat(12)
    TestAdat.newInstance(arrayOf<Any?>(12))
    return "OK"
}