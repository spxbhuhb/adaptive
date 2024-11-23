package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.load
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.save
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.ListWireFormat
import `fun`.adaptive.wireformat.builtin.LongWireFormat
import kotlinx.io.files.Path

class ItemFilePersistence<IT : AdatClass>(
    val path: Path,
    wireFormatProvider: WireFormatProvider,
) : AutoItemPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoItemExport<IT> {
        if (! path.exists()) return AutoItemExport.none()

        val meta = metaPath(path).load(wireFormatProvider, AutoMetadata.adatWireFormat)
        val (itemId, propertyTimes, value) = read<IT>(path, wireFormatProvider)

        return AutoItemExport(meta, itemId, propertyTimes, value)
    }

    override fun save(export: AutoItemExport<IT>) {
        val meta = checkNotNull(export.meta)

        save(metaPath(path), meta, wireFormatProvider)

        write(path, wireFormatProvider, export)
    }

//    override fun onRemove() {
//        deleteMeta(path)
//    }

    companion object {

        fun <IT : AdatClass> write(path: Path, wireFormatProvider: WireFormatProvider, export: AutoItemExport<IT>) {

            val itemId = checkNotNull(export.itemId) { "export without item id" }
            val propertyTimes = checkNotNull(export.propertyTimes) { "export without property times" }
            val value = checkNotNull(export.item) { "export without value" }

            val times = mutableListOf<Long>()

            for (propertyTime in propertyTimes) {
                times += propertyTime.peerId
                times += propertyTime.timestamp
            }

            @Suppress("UNCHECKED_CAST")
            val bytes = wireFormatProvider
                .encoder()
                .pseudoInstanceStart()
                .string(1, "type", value.adatCompanion.wireFormatName)
                .instanceOrNull(2, "itemId", itemId, LamportTimestamp)
                .instance(3, "properties", value, value.adatCompanion as AdatCompanion<IT>)
                .instance(4, "propertyTimes", times, ListWireFormat(LongWireFormat))
                .pseudoInstanceEnd()
                .pack()

            path.write(bytes)
        }

        fun <IT : AdatClass> read(path: Path, wireFormatProvider: WireFormatProvider): Triple<ItemId?, List<LamportTimestamp>, IT> {
            val decoder = wireFormatProvider.decoder(path.read())

            val type = decoder.string(1, "type")
            val itemId = decoder.instanceOrNull(2, "itemId", LamportTimestamp)

            @Suppress("UNCHECKED_CAST")
            val wireFormat = requireNotNull(WireFormatRegistry[type] as? WireFormat<AdatClass>) { "missing wire format for $type" }

            @Suppress("UNCHECKED_CAST")
            val value = decoder.instance<AdatClass>(3, "properties", wireFormat) as IT

            @Suppress("UNCHECKED_CAST")
            val times = decoder.instance(4, "propertyTimes", ListWireFormat(LongWireFormat)) as List<Long>

            val propertyTimes = mutableListOf<LamportTimestamp>()

            for (i in times.indices step 2) {
                propertyTimes += LamportTimestamp(times[i], times[i + 1])
            }

            return Triple(itemId, propertyTimes, value)
        }
    }
}