package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.FileFrontend.Companion.write
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class FolderFrontend<A : AdatClass<A>>(
    backend: SetBackend,
    companion: AdatCompanion<A>,
    onListCommit: ((newValue: List<A>) -> Unit)?,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)?,
    val wireFormatProvider: WireFormatProvider,
    val path: Path,
    val fileNameFun: (itemId: ItemId, item: A) -> String
) : AdatClassListFrontend<A>(
    backend,
    companion,
    onListCommit,
    onItemCommit
) {

    fun pathFor(itemId: ItemId, instance: A) =
        Path(path, fileNameFun(itemId, instance))

    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
    override fun getItemFrontend(itemId: ItemId) =

        classFrontends.getOrPut(itemId) {

            val propertyBackend = checkNotNull(backend.items[itemId])

            @Suppress("UNCHECKED_CAST")
            val wireFormat = propertyBackend.wireFormat as AdatClassWireFormat<A>

            val instance = wireFormat.newInstance(propertyBackend.values)
            val path = pathFor(itemId, instance)

            if (! path.exists()) {
                write(path, wireFormatProvider, itemId, instance)
            }

            FileFrontend(
                propertyBackend,
                wireFormat,
                itemId,
                instance,
                this,
                null,
                wireFormatProvider,
                path
            )
                .also { propertyBackend.frontEnd = it }
        }

    companion object {

        fun load(context: BackendContext, path: Path, wireFormatProvider: WireFormatProvider): Map<ItemId, PropertyBackend> {
            require(path.exists()) { "path $path does not exist" }

            val result = mutableMapOf<ItemId, PropertyBackend>()

            SystemFileSystem.list(path).forEach {

                // ignore hidden files
                if (it.name.startsWith(".")) return@forEach

                val (itemId, instance) = FileFrontend.read(it, wireFormatProvider)
                checkNotNull(itemId)

                check(itemId !in result) { "duplicated item id $itemId in $it" }

                result[itemId] = PropertyBackend(
                    context,
                    itemId,
                    instance.adatCompanion.wireFormatName,
                    instance.toArray()
                )
            }

            return result
        }
    }
}