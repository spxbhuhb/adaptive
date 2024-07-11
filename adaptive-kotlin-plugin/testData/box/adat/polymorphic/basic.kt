package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*
import hu.simplexion.adaptive.service.testing.TestServiceTransport
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import hu.simplexion.adaptive.wireformat.toJson

@Adat
class T1(
    var someInt: Int
)

@Adat
class T2(
    var someInt: Int
)

@Adat
class TL(
    val someList: List<AdatClass<*>>
)

fun box(): String {
    val tl = TL(listOf(T1(12), T2(23)))

    val json = tl.toJson(tl.adatCompanion.adatWireFormat)

    val tlr = tl.adatCompanion.fromJson(json)

    if (tl != tlr) return "Fail: ${tlr}"

    return "OK"
}