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
) : CollectionBackendBase(context.handle.peerId) {

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
        val item = items[operation.itemId]
        if (item != null) {
            item.modify(operation, commit, distribute)
        } else {
            // This is a tricky situation, we've got a modification, but we don't have the
            // item yet. May happen if the item is updated during synchronization. In this
            // case we have to put this operation to the shelf until the synchronization
            // is done.
            afterSync += operation
        }
    }

    override fun empty(operation: AutoEmpty, commit: Boolean, distribute: Boolean) {
        closeListOp(operation, emptySet(), commit, distribute)
    }

    override fun syncEnd(operation: AutoSyncEnd, commit: Boolean, distribute: Boolean) {
        // apply modifications that have been postponed because we haven't had the
        // item at the time (see syncPeer and modify for details)
        afterSync.forEach { modify(it, commit, distribute) }
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

            if (itemId > syncFrom) {
                connector.send(AutoAdd(itemId, itemId, item.wireFormatName,  null, encode(item.wireFormatName, item.values)))
            } else {
                item.syncPeer(connector, syncFrom)
            }
        }

        connector.send(AutoSyncEnd(time))

        trace { "SYNC END:  --SENT--  time=$time peerTime=$syncFrom" }
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: AdatClass<*>) {
        // this is here for safety, theoretically the item id should never be in removals when addItem is called
        if (itemId in removals) return

        additions += itemId
        val backend = PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, value.toArray())
        items += itemId to backend
    }

}