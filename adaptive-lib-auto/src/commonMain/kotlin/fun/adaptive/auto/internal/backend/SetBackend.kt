package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*

class SetBackend<A : AdatClass>(
    override val context: BackendContext<A>,
    initialValue: Map<ItemId, PropertyBackend<A>>? = null
) : CollectionBackendBase<A>(context.handle) {

    val additions = mutableSetOf<ItemId>()
    val removals = mutableSetOf<ItemId>()

    override val items = initialValue?.toMutableMap() ?: mutableMapOf<ItemId, PropertyBackend<A>>()

    init {
        if (initialValue != null && initialValue.isNotEmpty()) {
            additions.addAll(initialValue.keys)
            context.receive(LamportTimestamp(context.handle.peerId, initialValue.keys.maxOf { it.timestamp }))
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun remove(itemId: ItemId, commit: Boolean) {
        removals += itemId

        items.remove(itemId)?.removed()

        val operation = AutoRemove(context.nextTime(), setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        close(operation, commit)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean) {
        removals += itemIds

        for (itemId in itemIds) {
            items.remove(itemId)?.removed()
        }

        val operation = AutoRemove(context.nextTime(), itemIds)
        trace { "FE -> BE  commit true .. $operation" }

        close(operation, commit)
    }

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = items[itemId] ?: return
        item.modify(itemId, propertyName, propertyValue)
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun add(operation: AutoAdd, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        @Suppress("UNCHECKED_CAST")
        addItem(operation.itemId, null, decode(operation.wireFormatName, operation.payload) as A)

        closeListOp(operation, setOf(operation.itemId), commit)

        context.receive(operation.itemId)
    }

    override fun remove(operation: AutoRemove, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        removals += operation.itemIds
        for (itemId in operation.itemIds) {
            val item = items.remove(itemId)
            item?.frontend?.removed()
        }

        closeListOp(operation, operation.itemIds, commit)
    }

    override fun modify(operation: AutoModify, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        if (operation.itemId in removals) return

        val item = items[operation.itemId]

        if (item != null) {
            item.modify(operation, commit)
        } else {
            // This is a tricky situation, we've got a modification, but we don't have the
            // item yet. May happen if the item is updated during synchronization. In this
            // case we have to put this operation to the shelf until the synchronization
            // is done.
            afterSync += operation
        }
    }

    override fun empty(operation: AutoEmpty, commit: Boolean) {
        closeListOp(operation, emptySet(), commit)
        context.receive(operation.timestamp)
    }

    override fun syncEnd(operation: AutoSyncEnd, commit: Boolean) {
        // apply modifications that have been postponed because we haven't had the
        // item at the time (see syncPeer and modify for details)
        afterSync.forEach { modify(it, commit) }
        afterSync.clear()
        context.receive(operation.timestamp)
        trace { "time=${context.time}" }
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    override suspend fun syncPeer(connector: AutoConnector, syncFrom: LamportTimestamp) {

        // The current time marks everything we have to send over. Anything happens after
        // this point will be sent by the normal distribution mechanism. The only problematic
        // events are the property updates as the updated item may not be on the other side yet.
        // `modify` and `syncEnd` covers that situation

        val time = context.time

        trace { "SYNC START: time=$time peerTime=$syncFrom" }

        if (syncFrom.timestamp >= time.timestamp) {
            trace { "SYNC END:  --SKIPPED--  time=$time peerTime=$syncFrom" }
            return
        }

        // When there is an item id in the connector, the peer is a single-item peer.
        // In that case we have to treat it such: cannot send over list operations.

        if (connector.peerHandle.itemId != null) {
            syncItem(connector, syncFrom, connector.peerHandle.itemId)
        } else {
            syncCollection(time, connector, syncFrom)
        }

        trace { "SYNC END:  --SENT--  time=$time peerTime=$syncFrom" }
    }

    suspend fun syncItem(connector: AutoConnector, syncFrom: LamportTimestamp, itemId: ItemId) {
        requireNotNull(items[itemId]) { "missing item: $itemId" }
            .syncPeer(connector, syncFrom)
    }

    suspend fun syncCollection(time : LamportTimestamp, connector: AutoConnector, syncFrom: LamportTimestamp) {
        if (additions.isEmpty()) {
            connector.send(AutoEmpty(time))
            trace { "SYNC END:  --EMPTY--  time=$time peerTime=$syncFrom" }
            return
        }

        if (removals.isNotEmpty()) {
            connector.send(AutoRemove(time, removals))
        }

        for (item in items.values.sortedBy { it.itemId }) {
            val itemId = item.itemId

            if (itemId.timestamp > syncFrom.timestamp) {
                connector.send(AutoAdd(time, itemId, item.wireFormatName, null, encode(item.wireFormatName, item.values)))
            } else {
                item.syncPeer(connector, syncFrom)
            }
        }

        connector.send(AutoSyncEnd(time))
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: A) {
        // this is here for safety, theoretically the item id should never be in removals when addItem is called
        if (itemId in removals) return

        additions += itemId

        val values = value.toArray()
        val backend = PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, values)

        items += itemId to backend

        context.onAdd(value)
    }

}