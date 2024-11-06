package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.origin.AutoItem
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.load
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.ListWireFormat
import `fun`.adaptive.wireformat.builtin.LongWireFormat
import kotlinx.io.files.Path

class ItemFilePersistence<IT : AdatClass>(
    val path: Path,

    wireFormatProvider: WireFormatProvider
) : ItemPersistence<IT>(wireFormatProvider) {

    override fun load(): ItemLoad {
        if (! path.exists()) return ItemLoad.NONE

        val meta = loadMeta(path)

        val (itemId, propertyTimes, value) = read<IT>(path, wireFormatProvider)

        return ItemLoad(meta, itemId, propertyTimes, value.toArray())
    }

    override fun onChange(itemId : ItemId, newValue: IT, oldValue: IT?) {
        write(path, wireFormatProvider, itemId)
    }

    override fun onRemove() {
        deleteMeta(path)
    }

    companion object {

        fun <A : AdatClass> write(path: Path, wireFormatProvider: WireFormatProvider, itemId: ItemId, propertyTimes: List<LamportTimestamp>, value: A) {

            val times = mutableListOf<Long>()

            for (propertyTime in propertyTimes) {
                times += propertyTime.peerId.value
                times += propertyTime.timestamp
            }

            @Suppress("UNCHECKED_CAST")
            val bytes = wireFormatProvider
                .encoder()
                .pseudoInstanceStart()
                .string(1, "type", value.adatCompanion.wireFormatName)
                .instanceOrNull(2, "itemId", itemId.value, LamportTimestamp)
                .instance(3, "properties", value, value.adatCompanion as AdatCompanion<A>)
                .instance(4, "propertyTimes", times, ListWireFormat(LongWireFormat))
                .pseudoInstanceEnd()
                .pack()

            path.write(bytes)
        }

        fun <IT : AdatClass> read(path: Path, wireFormatProvider: WireFormatProvider): Triple<ItemId?, List<LamportTimestamp>, IT> {
            val decoder = wireFormatProvider.decoder(path.read())

            val type = decoder.string(1, "type")
            val itemId = decoder.instanceOrNull(2, "itemId", LamportTimestamp)?.let { ItemId(it) }

            @Suppress("UNCHECKED_CAST")
            val wireFormat = requireNotNull(WireFormatRegistry[type] as? WireFormat<AdatClass>) { "missing wire format for $type" }

            @Suppress("UNCHECKED_CAST")
            val value = decoder.instance<AdatClass>(3, "properties", wireFormat) as IT

            @Suppress("UNCHECKED_CAST")
            val times = decoder.instance(4, "propertyTimes", ListWireFormat(LongWireFormat)) as List<Long>

            val propertyTimes = mutableListOf<LamportTimestamp>()

            for (i in times.indices step 2) {
                propertyTimes += LamportTimestamp(PeerId(times[i]), times[i + 1])
            }

            return Triple(itemId, propertyTimes, value)
        }
    }
}