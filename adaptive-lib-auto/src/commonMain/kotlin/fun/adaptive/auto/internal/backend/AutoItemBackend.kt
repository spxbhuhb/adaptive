package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId

abstract class AutoItemBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>
) : AutoBackend<IT>(instance) {

    abstract var itemId: ItemId

    abstract val wireFormat: AdatClassWireFormat<*>

    abstract fun getItem(): IT

//    fun update(propertyName: String, propertyValue: Any?) {
//        update(itemId, propertyName, propertyValue)
//    }
//
//    fun update(path: Array<String>, value: Any?) {
//        check(path.size == 1) { "multi-level paths are not implemented yet" }
//        update(itemId, path[0], value)
//    }
//
//    fun update(new: IT) {
//        update(itemId, new)
//    }

    /**
     * Called by collection backends when the item of this backend is removed from the collection.
     */
    fun removed(fromBackend: Boolean) {
        // FIXME frontend?.removed(fromBackend)
    }

    internal abstract fun encode(): ByteArray
}