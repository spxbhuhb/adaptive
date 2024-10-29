package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.origin.AutoInstance

abstract class AutoFrontend<IT : AdatClass> : AdatStore<AdatClass>() {

    abstract val instance: AutoInstance<*, *, *, IT>

    abstract fun commit(initial: Boolean, fromBackend: Boolean)

    /**
     * Called by collection backends when the item that represented by this
     * frontend is removed. If the frontend persists the data somehow, it
     * should remove the persisted data when this function is called.
     */
    open fun removed(fromBackend: Boolean) {

    }

}