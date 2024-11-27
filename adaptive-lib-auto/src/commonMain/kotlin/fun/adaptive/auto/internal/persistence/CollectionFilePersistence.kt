package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.utility.load
import `fun`.adaptive.utility.save
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class CollectionFilePersistence<IT : AdatClass>(
    val path: Path,
    wireFormatProvider: WireFormatProvider,
    val includeFun: (Path) -> Boolean = { true },
) : AutoCollectionPersistence<IT>(
    wireFormatProvider
) {

    override fun load(): AutoCollectionExport<IT> {
        val meta = Path(path).load(wireFormatProvider, AutoMetadata.adatWireFormat)

        val result = mutableListOf<AutoItemExport<IT>>()

        SystemFileSystem.list(path).forEach {

            if (it.name.startsWith(".")) return@forEach  // ignore hidden files
            if (! includeFun(it)) return@forEach

            ItemFilePersistence.read<IT>(it, wireFormatProvider)
        }

        return AutoCollectionExport(meta, result)
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