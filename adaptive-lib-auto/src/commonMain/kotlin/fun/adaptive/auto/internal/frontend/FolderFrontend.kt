package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path

class FolderFrontend<A : AdatClass<A>>(
    backend: SetBackend,
    companion: AdatCompanion<A>,
    onListCommit: ((newValue: List<A>) -> Unit)?,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)?,
    val wireFormatProvider: WireFormatProvider,
    val path: (itemId: ItemId, item: A) -> Path
) : AdatClassListFrontend<A>(
    backend,
    companion,
    onListCommit,
    onItemCommit
) {

    override fun update(instance: AdatClass<*>, path: Array<String>, value: Any?) {
        // FIXME only single properties are handled b y AdatClassListFrontend
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(instance.adatContext !!.id as ItemId, path[0], value)
    }

    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
    override fun getFrontend(itemId: ItemId) =
        classFrontends.getOrPut(itemId) {

            val propertyBackend = checkNotNull(backend.items[itemId])

            @Suppress("UNCHECKED_CAST")
            val wireFormat = propertyBackend.wireFormat as AdatClassWireFormat<A>

            val instance = wireFormat.newInstance(propertyBackend.values)

            FileFrontend(
                propertyBackend,
                wireFormat,
                itemId,
                instance,
                this,
                null,
                wireFormatProvider,
                path(itemId, instance)
            )
                .also { propertyBackend.frontEnd = it }
        }

}