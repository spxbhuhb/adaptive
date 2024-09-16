package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Auto frontend to store properties in a file specified by [path].
 */
class FileFrontend<A : AdatClass>(
    backend: PropertyBackend,
    wireFormat: AdatClassWireFormat<A>,
    itemId: ItemId?,
    initialValue: A?,
    collectionFrontend: CollectionFrontendBase?,
    onCommit: ((frontend: AdatClassFrontend<A>) -> Unit)?,
    val wireFormatProvider: WireFormatProvider,
    val path: Path
) : AdatClassFrontend<A>(
    backend,
    wireFormat,
    initialValue,
    itemId,
    collectionFrontend,
    onCommit
) {

    override fun commit() {
        super.commit()
        write(path, wireFormatProvider, itemId, value !!)
    }

    override fun removed() {
        SystemFileSystem.delete(path)
    }

    companion object {

        fun <A : AdatClass> write(path: Path, wireFormatProvider: WireFormatProvider, itemId: ItemId?, value: A) {

            @Suppress("UNCHECKED_CAST")
            val bytes = wireFormatProvider
                .encoder()
                .pseudoInstanceStart()
                .string(1, "type", value.adatCompanion.wireFormatName)
                .instanceOrNull(2, "itemId", itemId, ItemId)
                .instance(3, "properties", value, value.adatCompanion as AdatCompanion<A>)
                .pseudoInstanceEnd()
                .pack()

            path.write(bytes)
        }

        fun read(path: Path, wireFormatProvider: WireFormatProvider): Pair<ItemId?, AdatClass> {
            val decoder = wireFormatProvider.decoder(path.read())

            val type = decoder.string(1, "type")
            val itemId = decoder.instanceOrNull(2, "itemId", ItemId)

            @Suppress("UNCHECKED_CAST")
            val wireFormat = requireNotNull(WireFormatRegistry[type] as? WireFormat<AdatClass>) { "missing wire format for $type" }

            val value = decoder.instance<AdatClass>(3, "properties", wireFormat)

            return itemId to value
        }
    }

}