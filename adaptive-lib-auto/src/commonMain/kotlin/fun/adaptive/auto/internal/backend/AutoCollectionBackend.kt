package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.frontend.AutoCollectionFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncEnd

abstract class AutoCollectionBackend<IT : AdatClass>(
    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>
) : AutoBackend<IT>(instance) {

    /**
     * Contains modification that should be applied when [AutoSyncEnd] arrives.
     */
    protected val afterSync = mutableListOf<AutoModify>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    fun add(item: IT, parentItemId: ItemId?) {
        val time = instance.nextTime()
        val itemId = time.asItemId()

        val item = addItem(itemId, parentItemId, item, false)

        val operation = AutoAdd(
            timestamp = time,
            itemId = itemId,
            item.wireFormat.wireFormatName,
            parentItemId,
            item.encode()
        )

        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        instance.commit(null, initial = false, fromPeer = false)

        distribute(operation)
    }

    abstract fun remove(itemId: ItemId, commit: Boolean)

    abstract fun removeAll(itemIds: Set<ItemId>, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun empty(operation: AutoEmpty, commit: Boolean)

    abstract fun add(operation: AutoAdd, commit: Boolean)

    abstract fun remove(operation: AutoRemove, commit: Boolean)

    // --------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------

    protected abstract fun addItem(
        itemId: ItemId,
        parentItemId: ItemId?,
        value: IT,
        fromBackend: Boolean
    ) : AutoItemBackend<IT>

}