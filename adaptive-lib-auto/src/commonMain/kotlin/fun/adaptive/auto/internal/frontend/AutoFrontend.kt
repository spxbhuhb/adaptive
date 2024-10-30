package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoHandle

abstract class AutoFrontend<VT, IT : AdatClass> : AdatStore<AdatClass>() {

    abstract val instance: AutoInstance<*, *, VT, IT>

    abstract var handle: AutoHandle?

    abstract var valueOrNull: VT?

    /**
     * When true, this frontend saves data and is able to load the last
     * saved value during instance creation.
     */
    abstract val persistent: Boolean

    abstract fun commit(initial: Boolean, fromBackend: Boolean)

    /**
     * Called by collection backends when the item that represented by this
     * frontend is removed. If the frontend persists the data somehow, it
     * should remove the persisted data when this function is called.
     */
    open fun removed(fromBackend: Boolean) {

    }

    /**
     * An operation to be implemented by persistent frontends such as file or database.
     * During instance creation this function is called to load the handle and the content
     * of the auto instance if possible.
     *
     * The function have to set [handle] and [valueOrNull] to the appropriate value if
     * possible or leave them on null if they are unknown.
     */
    abstract fun load()

}