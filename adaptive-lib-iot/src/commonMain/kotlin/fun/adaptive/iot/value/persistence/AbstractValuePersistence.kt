package `fun`.adaptive.iot.value.persistence

import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.lib.util.path.UuidFileStore
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat
import `fun`.adaptive.wireformat.json.JsonWireFormatDecoder
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import kotlinx.io.files.Path

abstract class AbstractValuePersistence {

    abstract fun loadValues(map : MutableMap<AioValueId, AioValue>)

    abstract fun saveValue(value : AioValue)

}