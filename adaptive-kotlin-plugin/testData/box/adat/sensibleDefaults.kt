package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatRegistry

enum class E {
    E1,
    E2
}

@Adat
class TestAdat(
    val i: Int,
    val ui: UInt,
    val e: E
)

fun box(): String {
    val t1 = TestAdat()

    if (t1.i != 0) return "Fail: t1.i = ${t1.i}"
    if (t1.ui != 0u) return "Fail: t1.ui = ${t1.ui}"
    if (t1.e != E.E1) return "Fail: t1.e = ${t1.e}"

    return "OK"
}