package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*

class SetBackend(
    override val context: BackendContext,
    initialValue: Map<ItemId, PropertyBackend>? = null
) : CollectionBackendBase(context.handle.clientId) {

    val additions = mutableSetOf<ItemId>()
    val removals = mutableSetOf<ItemId>()

    override val items = initialValue?.toMutableMap() ?: mutableMapOf<ItemId, PropertyBackend>()

    override val defaultWireFormatName = context.defaultWireFormat.wireFormatName

    init {
        if (initialValue != null && initialValue.isNotEmpty()) {
            additions.addAll(initialValue.keys)
            context.receive(initialValue.keys.maxOf { it })
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean) {
        removals += itemId

        items.remove(itemId)?.removed()

        val operation = AutoRemove(context.nextTime(), setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean) {
        removals += itemIds

        for (itemId in itemIds) {
            items.remove(itemId)?.removed()
        }

        val operation = AutoRemove(context.nextTime(), itemIds)
        trace { "FE -> BE  commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = items[itemId] ?: return
        item.modify(itemId, propertyName, propertyValue)
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun add(operation: AutoAdd, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        addItem(operation.itemId, null, decode(operation.wireFormatName, operation.payload))

        closeListOp(operation, setOf(operation.itemId), commit, distribute)
    }

    override fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        items -= operation.itemIds
        removals += operation.itemIds

        closeListOp(operation, operation.itemIds, commit, distribute)
    }

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        if (operation.itemId in removals) return
        val item = items[operation.itemId] ?: return
        item.modify(operation, commit, distribute)
    }

    override fun empty(operation: AutoEmpty, commit: Boolean, distribute: Boolean) {
        closeListOp(operation, emptySet(), commit, distribute)
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    override suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp) {
        val time = context.time

        if (peerTime.timestamp >= time.timestamp) {
            trace { "SKIP SYNC: time=$time peerTime=$peerTime" }
            return
        }

        if (additions.isEmpty()) {
            connector.send(AutoEmpty(time))
        }

        if (removals.isNotEmpty()) {
            connector.send(AutoRemove(time, removals))
        }

        for (item in items.values.sortedBy { it.itemId }) {
            val itemId = item.itemId

            if (itemId > peerTime) {
                connector.send(AutoAdd(itemId, itemId, item.wireFormatName,  null, encode(item.wireFormatName, item.values)))
            } else {
                item.syncPeer(connector, peerTime)
            }
        }

        trace { "SYNC DONE: time=$time peerTime=$peerTime" }
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: AdatClass<*>) {
        additions += itemId
        val backend = PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, value.toArray())
        items += itemId to backend
    }

}