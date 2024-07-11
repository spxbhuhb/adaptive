package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat()

fun box(): String {
    val t1 = TestAdat()
    return "OK"
}