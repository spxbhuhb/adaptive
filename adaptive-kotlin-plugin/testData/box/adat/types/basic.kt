package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.backend.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adat
class TestAdat(
    var stuff: Map<String, Int?>
)

fun box(): String {
    val t1 = TestAdat(mapOf("12" to 23))

    if (WireFormatRegistry["fun.adaptive.adat.TestAdat"] == null) return "Fail: not in WireFormatRegistry"

    return "OK"
}