package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.descriptor.kotlin.integer.*
import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.backend.builtin.ServiceImpl
import kotlinx.coroutines.runBlocking
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.adat.api.properties

@Adat
class TestAdat(
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) {
    override fun descriptor() {
        properties {
            someInt minimum 23 maximum 200 default 34
        }
    }
}

fun box(): String {
    val descriptorSets = TestAdat.adatDescriptors

    if (descriptorSets.size != 3) return "Fail: descriptorSets.size != 3"

    val pds = descriptorSets[0]

    if (pds.property.name != "someInt") return "Fail: d.property.name != someInt"

    val ds = pds.descriptors

    if (ds.size != 3) return "Fail: ds.size != 3"

    if (ds[0] !is IntMinimum) return "Fail: ds[0] !is IntMinimum"

    return "OK"
}