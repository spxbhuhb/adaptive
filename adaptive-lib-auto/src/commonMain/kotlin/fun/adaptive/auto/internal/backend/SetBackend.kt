package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class SetBackend<A : AdatClass>(
    override val context: BackendContext<A>,
    initialValue: MutableMap<ItemId, PropertyBackend<A>>? = null
) : CollectionBackendBase<A>(context.handle) {

    val data = SetBackendData<A>(initialValue)

    init {
        if (initialValue != null && initialValue.isNotEmpty()) {
            data.addAll(initialValue.keys)
            context.receive(LamportTimestamp(context.handle.peerId, initialValue.values.maxOf { it.lastUpdate.timestamp }))
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun remove(itemId: ItemId, commit: Boolean) {
        data -= itemId

        val operation = AutoRemove(context.nextTime(), true, setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        close(operation, commit)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean) {
        data -= itemIds

        val operation = AutoRemove(context.nextTime(), true, itemIds)
        trace { "FE -> BE  commit true .. $operation" }

        close(operation, commit)
    }

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = data[itemId] ?: return
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

        data -= operation.itemIds

        closeListOp(operation, operation.itemIds, commit)

        if (operation.syncTime) {
            context.receive(operation.timestamp)
        }
    }

    override fun modify(operation: AutoModify, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        if (operation.itemId !in data) return

        val item = data[operation.itemId]

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
        context.onSyncEnd()
        trace { "time=${context.time}" }
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    override suspend fun syncPeer(
        connector: AutoConnector,
        syncFrom: LamportTimestamp,
        syncBatch: MutableList<AutoModify>?,
        sendSyncEnd: Boolean,
    ) {
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

        if (sendSyncEnd) {
            connector.send(AutoSyncEnd(time))
        }

        trace { "SYNC END:  --SENT--  time=$time peerTime=$syncFrom" }
    }

    suspend fun syncItem(connector: AutoConnector, syncFrom: LamportTimestamp, itemId: ItemId) {
        requireNotNull(data[itemId]) { "missing item: $itemId" }
            .syncPeer(connector, syncFrom, null, false)
    }

    suspend fun syncCollection(time: LamportTimestamp, connector: AutoConnector, syncFrom: LamportTimestamp) {
        if (data.isEmpty()) {
            connector.send(AutoEmpty(time))
            trace { "SYNC END:  --EMPTY--  time=$time peerTime=$syncFrom" }
            return
        }

        val removals = data.removals()
        if (removals.isNotEmpty()) {
            connector.send(AutoRemove(time, false, removals))
        }

        var add = mutableListOf<AutoAdd>()
        val modify = mutableListOf<AutoModify>()

        for (item in data.items()) {
            val itemId = item.itemId

            if (itemId.timestamp > syncFrom.timestamp) {
                add += AutoAdd(time, itemId, item.wireFormatName, null, encode(item.wireFormatName, item.values))
            } else {
                item.syncPeer(connector, syncFrom, modify, false)
            }

            if (add.size + modify.size >= 1000) {
                connector.send(AutoSyncBatch(time, add, modify))
                add.clear()
                modify.clear()
            }
        }

        if (add.isNotEmpty() || modify.isNotEmpty()) {
            connector.send(AutoSyncBatch(time, add, modify))
        }
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: A) {

        val values = value.toArray()
        val backend = PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, values)

        val added = data.add(itemId, backend)

        if (added) {
            context.onChange(value, null)
        }
    }

}