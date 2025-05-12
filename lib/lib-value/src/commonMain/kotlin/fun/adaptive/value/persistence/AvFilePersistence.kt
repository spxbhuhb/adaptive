package `fun`.adaptive.value.persistence

import `fun`.adaptive.lib.util.path.UuidFileStore
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.*
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat
import `fun`.adaptive.wireformat.json.JsonWireFormatDecoder
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import kotlinx.coroutines.*
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.minutes

class FilePersistence(
    root: Path,
    levels: Int,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val useWriteCache: Boolean = false
) : AbstractValuePersistence() {

    val lock = getLock()
    val logger = getLogger("FilePersistence")

    var writeCache = mutableMapOf<AvValueId, AvValue<*>>()

    val store = object : UuidFileStore<MutableMap<AvValueId, AvValue<*>>>(root, levels) {

        override fun loadPath(path: Path, map: MutableMap<AvValueId, AvValue<*>>) {
            if (! path.name.endsWith(".json")) return

            try {
                val bytes = path.read()
                val decoder = JsonWireFormatDecoder(bytes)
                val value = PolymorphicWireFormat.wireFormatDecode(decoder.root, decoder)
                check(value is AvValue<*>) { "Value is not an AvValue" }

                map[value.uuid] = value

            } catch (ex: Exception) {
                throw RuntimeException("error while loading value from $path", ex)
            }

        }

    }

    override fun loadValues(map: MutableMap<AvValueId, AvValue<*>>) {
        if (useWriteCache) {
            scope.launch {
                while (isActive) {
                    flushCache()
                    delay(5.minutes)
                }
            }
        }

        store.loadAll(map)
    }

    override fun saveValue(value: AvValue<*>) {
        if (! useWriteCache) {
            writeValue(value)
        } else {
            lock.use {
                writeCache[value.uuid] = value
            }
        }
    }

    fun writeValue(value: AvValue<*>) {
        val bytes = PolymorphicWireFormat.wireFormatEncode(JsonWireFormatEncoder(), value).pack()
        val dirPath = store.pathFor(value.uuid).ensure()
        val filePath = dirPath.resolve("${value.uuid}.json")

        filePath.write(bytes, overwrite = true, useTemporaryFile = true)
    }

    fun flushCache() {

        val out = lock.use {
            val current = writeCache
            writeCache = mutableMapOf()
            current
        }

        for ((_, value) in out) {
            writeValue(value)
        }

        logger.info { "Flushed ${out.size} value(s) to disk." }
    }

}