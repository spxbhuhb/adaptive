package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.load
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

abstract class ItemPersistence<IT : AdatClass>(
    val wireFormatProvider: WireFormatProvider
) : AutoItemListener<IT>() {

    abstract fun load(): ItemLoad

    fun loadMeta(path: Path): AutoMetadata<IT> =
        metaPath(path).load<AutoMetadata<IT>>(wireFormatProvider)

    fun deleteMeta(path: Path) {
        SystemFileSystem.delete(metaPath(path))
    }

    fun metaPath(path: Path) : Path {
        val isDirectory = (SystemFileSystem.metadataOrNull(path)?.isDirectory == true)

        val path = if (! isDirectory) {
            val dir = checkNotNull(path.parent) { "cannot load the root directory" }
            Path(dir, "${path.name}.meta")
        } else {
            Path(path, "auto.meta")
        }

        check(path.exists()) { "inconsistent auto state: metadata file does not exists for $path" }

        return path
    }
}