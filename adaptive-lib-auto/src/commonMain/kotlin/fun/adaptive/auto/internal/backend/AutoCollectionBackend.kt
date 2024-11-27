package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoUpdate
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncEnd

abstract class AutoCollectionBackend<IT : AdatClass>(
    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionPersistence<IT>, Collection<IT>, IT>
) : AutoBackend<IT>(instance) {

    /**
     * Contains modification that should be applied when [AutoSyncEnd] arrives.
     */
    protected val afterSync = mutableListOf<AutoUpdate>()

    abstract fun firstOrNull(filterFun: (IT) -> Boolean): AutoItemBackend<IT>?

    abstract fun filter(filterFun : (IT) -> Boolean) : Collection<IT>

    internal abstract fun getItems() : Collection<IT>

}