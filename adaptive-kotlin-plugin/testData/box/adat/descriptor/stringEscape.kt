package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.descriptor.kotlin.string.*
import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.backend.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.wireformat.toJson

@Adat
class TestAdat(
    var someString: String
) {
    override fun descriptor() {
        properties {
            someString pattern "^a\\.b\$"
        }
    }
}

fun box(): String {
    val pattern = TestAdat.adatDescriptors[0].descriptors[0]

    if (pattern !is StringPattern) return "Fail: pattern !is StringPattern"
    if (pattern.pattern != "^a\\.b$") return "Fail: pattern != a.b (it is >${pattern.pattern}<)"

    return "OK"
}