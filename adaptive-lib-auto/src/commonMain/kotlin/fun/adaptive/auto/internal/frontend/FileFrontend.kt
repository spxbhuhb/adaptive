package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Auto frontend to store properties in a file specified by [path].
 */
class FileFrontend<A : AdatClass<A>>(
    backend: PropertyBackend,
    wireFormat: AdatClassWireFormat<A>,
    itemId: ItemId?,
    collectionFrontend: CollectionFrontendBase?,
    onCommit: ((frontend: AdatClassFrontend<A>) -> Unit)?,
    val wireFormatProvider: WireFormatProvider,
    val path: Path
) : AdatClassFrontend<A>(
    backend,
    wireFormat,
    null,
    itemId,
    collectionFrontend,
    onCommit
) {

    override fun commit() {
        super.commit()

        val bytes = wireFormatProvider.encode(value !!, wireFormat)

        SystemFileSystem.sink(path).buffered().use {
            it.write(bytes)
        }
    }

}