package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
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
import `fun`.adaptive.utility.save

class CollectionFilePersistence<IT : AdatClass>(
    val path: Path,
    wireFormatProvider: WireFormatProvider,
) : AutoCollectionPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoCollectionExport<IT> {
        val meta = Path(path).load(wireFormatProvider, AutoMetadata.adatWireFormat)

        val result = mutableMapOf<ItemId, IT>()
//
//            SystemFileSystem.list(path).forEach {
//
//                // ignore hidden files
//                if (it.name.startsWith(".")) return@forEach
//                if (! includeFun(it)) return@forEach
//
//                val (itemId, propertyTimes, instance) = FileFrontend.read<A>(it, wireFormatProvider)
//                checkNotNull(itemId)
//
//                check(itemId !in result) { "duplicated item id $itemId in $it" }
//
//                result[itemId] = PropertyBackend(
//                    context,
//                    itemId,
//                    instance.adatCompanion.wireFormatName,
//                    instance.toArray(),
//                    propertyTimes
//                )
//            }
    }

    override fun save(export: AutoCollectionExport<IT>) {
        checkNotNull(export.meta) { "export without meta" }
        save(path, export.meta, wireFormatProvider)
    }

// FIXME missing onRemove for ItemFilePersistence
//    override fun onRemove() {
//        deleteMeta(path)
//    }

}