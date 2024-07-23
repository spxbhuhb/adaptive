package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.model.ReturnException

@Adat
class TestAdat(
    val i: Int
) : ReturnException()

fun box(): String {
    TestAdat()
    TestAdat(12)
    TestAdat.newInstance()
    TestAdat.newInstance(arrayOf<Any?>(12))
    return "OK"
}