package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.utility.*
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.io.files.Path

/**
 * Stores a list of [TemporalIndexEntry] instances. The store is persisted into
 * a file selected by [path].
 *
 * - The file stores data in protobuf format.
 * - First protobuf message is [TemporalIndexHeader]
 * - All following entries are [TemporalIndexEntry]
 *
 * 1. This class is **NOT** thread safe.
 * 2. This class is intended for small lists, it stores all entries in memory.
 * 3. Each [append] call opens and closes the file, so it is somewhat expensive.
 * 4. When add returns the data is in the file.
 */
class TemporalIndexStore(
    val storeUuid: TemporalRecordStoreId,
    val path: Path,
    initialize: Boolean = true
) {

    companion object {
        const val V1 = 1
    }

    val entries = mutableListOf<TemporalIndexEntry>()

    var initialized: Boolean = false
        private set

    val latest
        get() = entries.lastOrNull()

    init {
        if (initialize) {
            initialize()
        }
    }

    fun initialize() {
        if (initialized) return

        if (! path.exists()) {
            path.parent?.ensure()
            path.write(TemporalIndexHeader(V1, storeUuid).encodeToProtoByteArray())
        } else {
            val (header, loaded) = load(path)
            check(header.storeUuid == storeUuid) { "uuid mismatch: ${header.storeUuid} != $storeUuid" }
            entries.addAll(loaded)
        }

        initialized = true
    }

    fun append(entry: TemporalIndexEntry) {
        entries += entry
        path.append(entry.encodeToProtoByteArray())
    }

    private fun load(path: Path): Pair<TemporalIndexHeader, MutableList<TemporalIndexEntry>> {
        val bytes = path.read()
        val decoder = ProtoWireFormatDecoder(bytes)

        val header = decoder.records[0].let {
            it.decoder().rawInstance(it, TemporalIndexHeader)
        }

        val entries = mutableListOf<TemporalIndexEntry>()

        for (i in 1 until decoder.records.size) {
            decoder.records[i].let {
                entries += it.decoder().rawInstance(it, TemporalIndexEntry)
            }
        }

        return header to entries
    }

}