package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.ListWireFormat
import `fun`.adaptive.wireformat.builtin.LongWireFormat
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Auto frontend to store properties in a file specified by [path].
 */
class FileFrontend<IT : AdatClass>(
    instance: AutoInstance<*, *, IT, IT>,
    initialValue: IT?,
    collectionFrontend: AutoCollectionFrontend<IT>?,
    itemId: ItemId = ItemId.CONNECTING,
    val path: Path,
) : AdatClassFrontend<IT>(
    instance, initialValue, collectionFrontend, itemId
) {

    lateinit var backend : AutoItemBackend<IT>

    override fun commit(initial : Boolean, fromPeer: Boolean) {
        super.commit(initial, fromPeer)
        write(path, instance.wireFormatProvider, itemId, instance.backend.propertyTimes, value)
    }

    override fun removed(fromBackend: Boolean) {
        super.removed(fromBackend)
        SystemFileSystem.delete(path)
    }

    companion object {

        fun <A : AdatClass> write(path: Path, wireFormatProvider: WireFormatProvider, itemId: ItemId, propertyTimes : List<LamportTimestamp>, value: A) {

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

        fun <A : AdatClass> read(path: Path, wireFormatProvider: WireFormatProvider): Triple<ItemId?, List<LamportTimestamp>, A> {
            val decoder = wireFormatProvider.decoder(path.read())

            val type = decoder.string(1, "type")
            val itemId = decoder.instanceOrNull(2, "itemId", LamportTimestamp)?.let { ItemId(it) }

            @Suppress("UNCHECKED_CAST")
            val wireFormat = requireNotNull(WireFormatRegistry[type] as? WireFormat<AdatClass>) { "missing wire format for $type" }

            @Suppress("UNCHECKED_CAST")
            val value = decoder.instance<AdatClass>(3, "properties", wireFormat) as A

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