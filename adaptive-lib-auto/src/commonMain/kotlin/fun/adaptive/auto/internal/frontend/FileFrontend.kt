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


    }

}