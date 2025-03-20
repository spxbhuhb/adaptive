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

class FilePersistence(
    root : Path,
    levels : Int,
    val map : MutableMap<AioValueId, AioValue>
) : UuidFileStore(root, levels) {

    override fun loadFile(path: Path) {
        if (!path.name.endsWith(".json")) return

        val bytes = path.read()
        val decoder = JsonWireFormatDecoder(bytes)
        val value = PolymorphicWireFormat.wireFormatDecode(decoder.root, decoder)
        check(value is AioValue)

        map[value.uuid] = value
    }

    fun saveValue(value : AioValue) {
        val bytes = PolymorphicWireFormat.wireFormatEncode(JsonWireFormatEncoder(), value).pack()
        val dirPath = pathFor(value.uuid).ensure()
        val filePath = dirPath.resolve("${value.uuid}.json")

        filePath.write(bytes, overwrite = true, useTemporaryFile = true)
    }

}