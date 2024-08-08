package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.MetadataId
import hu.simplexion.adaptive.auto.model.operation.AutoAdd
import hu.simplexion.adaptive.auto.model.operation.AutoRemove

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