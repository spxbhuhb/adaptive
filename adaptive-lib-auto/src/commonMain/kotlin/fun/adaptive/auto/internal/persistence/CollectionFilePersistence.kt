package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.delete
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.load
import `fun`.adaptive.utility.save
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class CollectionFilePersistence<IT : AdatClass>(
    val metaPath: Path,
    wireFormatProvider: WireFormatProvider,
    val includeFun: (Path) -> Boolean = { true },
    val pathFun : (itemId : ItemId, item : IT) -> Path
) : AutoCollectionPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoCollectionExport<IT>? {
        if (!metaPath.exists()) return null

        val meta = metaPath.load(wireFormatProvider, AutoMetadata.adatWireFormat)
        val metaName = metaPath.name

        val items = mutableListOf<AutoItemExport<IT>>()

        SystemFileSystem.list(metaPath.parent!!).forEach {

            if (it.name.startsWith(".")) return@forEach  // ignore hidden files
            if (it.name == metaName) return@forEach // ignore meta file
            if (! includeFun(it)) return@forEach

            items += ItemFilePersistence.read<IT>(it, wireFormatProvider)
        }

        return AutoCollectionExport(meta, items)
    }

    override fun update(export: AutoCollectionExport<IT>) {
        checkNotNull(export.meta) { "export without meta" }
        save(metaPath, export.meta, wireFormatProvider)
        for (item in export.items) {
            update(item)
        }
    }

    override fun add(export: AutoItemExport<IT>) {
        val itemPath = pathFun(export.itemId!!, export.item!!)
        ItemFilePersistence<IT>.write(itemPath, wireFormatProvider, export)
    }

    override fun update(export: AutoItemExport<IT>) {
        val itemPath = pathFun(export.itemId!!, export.item!!)
        ItemFilePersistence<IT>.write(itemPath, wireFormatProvider, export)
    }

    override fun remove(itemId: ItemId, item: IT?) {
        if (item == null) return // TODO think about null items in CollectionFilePersistence.remove
        pathFun(itemId, item).delete()
    }

}