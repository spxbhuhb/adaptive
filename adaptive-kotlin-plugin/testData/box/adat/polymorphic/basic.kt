package `fun`.adaptive.adat

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.server.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.toJson

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