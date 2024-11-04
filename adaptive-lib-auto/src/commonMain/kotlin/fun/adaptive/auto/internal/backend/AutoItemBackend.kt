package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId

abstract class AutoItemBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>,
    val frontend: AutoItemFrontend<IT>? = null,
) : AutoBackend<IT>(instance) {

    abstract val itemId : ItemId

    abstract fun getItem() : IT

    fun update(propertyName: String, propertyValue: Any?) {
        update(itemId, propertyName, propertyValue)
    }

    fun update(path: Array<String>, value: Any?) {
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        update(itemId, path[0], value)
    }

    fun update(new: IT) {
        update(itemId, new)
    }

    /**
     * Called by collection backends when the item of this backend is removed from the collection.
     */
    fun removed(fromBackend: Boolean) {
        frontend?.removed(fromBackend)
    }

}