package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance

abstract class AutoItemBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, *, IT>,
    val frontend: AutoItemFrontend<IT>? = null,
) : AutoBackend<IT>(instance) {

    /**
     * Called by collection backends when the item of this backend is removed from the collection.
     */
    fun removed(fromBackend: Boolean) {
        frontend?.removed(fromBackend)
    }

}