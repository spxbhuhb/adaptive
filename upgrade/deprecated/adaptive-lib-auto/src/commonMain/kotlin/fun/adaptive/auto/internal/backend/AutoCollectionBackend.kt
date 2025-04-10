package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.auto.model.operation.AutoUpdate

abstract class AutoCollectionBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, Collection<IT>, IT>
) : AutoBackend<IT>(instance) {

    /**
     * Contains modification that should be applied when [AutoSyncEnd] arrives.
     */
    protected val afterSync = mutableListOf<AutoUpdate>()

    abstract fun firstOrNull(filterFun: (IT) -> Boolean): AutoItemBackend<IT>?

    abstract fun filter(filterFun : (IT) -> Boolean) : Collection<IT>

    internal abstract fun getItems() : Collection<IT>

    internal abstract fun getItemsOrNull() : Collection<IT>?

    /**
     * @param  withItems  When true the export will contain the items. When false
     *                    the item list will be empty. `true` is used by persistence
     *                    initialization, `false` is used by persistence updates.
     */
    internal abstract fun export(withItems : Boolean) : AutoCollectionExport<IT>

    internal abstract fun exportItem(itemId: ItemId) : AutoItemExport<IT>?

}