package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId

abstract class AutoCollectionFrontend<IT : AdatClass>(
    override val instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>,
) : AutoFrontend<Collection<IT>, IT>() {

    abstract val values: Collection<IT>

    /**
     * @see AutoInstance.commit
     */
    abstract fun commit(itemBackend : AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean)

    abstract fun commit(itemId: ItemId, newValue: IT, oldValue: IT?, initial: Boolean, fromBackend: Boolean)

    abstract fun add(item: IT)

    abstract fun remove(itemId: ItemId)

    abstract fun remove(selector: (IT) -> Boolean)

}