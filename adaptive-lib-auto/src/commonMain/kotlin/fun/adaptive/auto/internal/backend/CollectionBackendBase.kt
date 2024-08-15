package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.MetadataId
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoRemove

abstract class CollectionBackendBase : BackendBase() {

    abstract val items: MutableMap<ItemId, PropertyBackend>

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    abstract fun add(item: AdatClass<*>, metadataId: MetadataId?, parentItemId: ItemId?, commit: Boolean, distribute: Boolean)

    abstract fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean)

    abstract fun removeAll(itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    abstract fun add(operation: AutoAdd, commit: Boolean, distribute: Boolean)

    abstract fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean)

}