package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

@Adat
class TestAdat(
    val someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
)

fun box(): String {
    val t1 = TestAdat()
    t1.someBoolean = true
    return if (t1.someBoolean) "OK" else "Fail"
}