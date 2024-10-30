package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId

abstract class AutoCollectionFrontend<IT : AdatClass>(
    override val instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, List<IT>, IT>,
) : AutoFrontend<IT>() {

    abstract var values: List<IT>
        protected set

    abstract fun commit(itemId: ItemId, newValue: IT, oldValue: IT?, initial: Boolean, fromBackend: Boolean)

    abstract fun add(item: IT)

    abstract fun remove(itemId: ItemId)

    abstract fun remove(selector: (IT) -> Boolean)

    override fun loadHandle(): AutoHandle? {
        return AutoHandle(collection = true)
    }
}