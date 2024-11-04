package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.frontend.AutoCollectionFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*

class SetBackend<IT : AdatClass>(
    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>,
    initialValue: MutableMap<ItemId, PropertyBackend<IT>>? = null,
) : AutoCollectionBackend<IT>(instance) {

    val data = SetBackendData<IT>(initialValue)

    init {
        if (initialValue != null && initialValue.isNotEmpty()) {
            data.addAll(initialValue.keys)
            instance.receive(LamportTimestamp(instance.handle.peerId, initialValue.values.maxOf { it.lastUpdate.timestamp }))
        }
    }

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    // add is defined in AutoCollectionBackend

    override fun remove(itemId: ItemId, commit: Boolean) {
        data.remove(itemId, fromPeer = false)

        val operation = AutoRemove(instance.nextTime(), true, setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        instance.commit(null, initial = false, fromPeer = false)
        distribute(operation)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean) {
        data.remove(itemIds, fromPeer = false)

        val operation = AutoRemove(instance.nextTime(), true, itemIds)
        trace { "FE -> BE  commit true .. $operation" }

        instance.commit(null, initial = false, fromPeer = false)
        distribute(operation)
    }

    override fun update(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = data[itemId] ?: return
        item.update(itemId, propertyName, propertyValue) // calls instance.commit and distribute
    }

    override fun update(itemId: ItemId, new : IT) {
        val item = data[itemId] ?: return
        item.update(itemId, new) // calls instance.commit and distribute
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun add(operation: AutoAdd, commit: Boolean) {
        trace { "BE -> BE  $operation" }

        @Suppress("UNCHECKED_CAST")
        addItem(operation.itemId, null, decode(operation.wireFormatName, operation.payload) as IT, fromBackend = true)

        instance.commit(null, initial = false, fromPeer = true)

        distribute(operation)

        instance.receive(operation.itemId)
    }

    override fun remove(operation: AutoRemove, commit: Boolean) {
        trace { "BE -> BE  $operation" }

        data.remove(operation.itemIds, fromPeer = true)

        instance.commit(null, initial = false, fromPeer = true)

        distribute(operation)

        if (operation.syncTime) {
            instance.receive(operation.timestamp)
        }
    }

    override fun update(operation: AutoModify, commit: Boolean) {
        if (operation.itemId !in data) return

        val item = data[operation.itemId]

        if (item != null) {
            item.update(operation, commit) // calls instance.commit and distribute, also trace
            instance.receive(operation.timestamp)
        } else {
            // This is a tricky situation, we've got a modification, but we don't have the
            // item yet. May happen if the item is updated during synchronization. In this
            // case we have to put this operation to the shelf until the synchronization
            // is done.
            trace { "BE -> BE  AFTER-SYNC: $operation" }
            afterSync += operation
        }
    }

    override fun empty(operation: AutoEmpty, commit: Boolean) {
        instance.commit(null, initial = false, fromPeer = true)

        distribute(operation)

        instance.receive(operation.timestamp)
    }

    override fun syncEnd(operation: AutoSyncEnd, commit: Boolean) {
        // apply modifications that have been postponed because we haven't had the
        // item at the time (see syncPeer and modify for details)
        afterSync.forEach { update(it, commit) } // calls instance.commit and distribute
        afterSync.clear()
        instance.receive(operation.timestamp)
        instance.onSyncEnd(fromBackend = true)
        trace { "time=${instance.time}" }
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

        val time = instance.time

        trace { "SYNC START: time=$time peerTime=$syncFrom" }

        if (syncFrom.timestamp >= time.timestamp) {
            trace { "SYNC END:  --SKIPPED--  time=$time peerTime=$syncFrom" }
            return
        }

        // When there is an item id in the connector, the peer is a single-item peer.
        // In that case we have to treat it such: cannot send over list operations.

        val itemId = connector.peerHandle.itemId
        if (itemId != null) {
            syncItem(connector, syncFrom, itemId)
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

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: IT, fromBackend: Boolean) {

        val values = value.toArray()
        val backend = PropertyBackend<IT>(instance, value.adatCompanion.wireFormatName, values, null, itemId = itemId)

        val added = data.add(itemId, backend)

        if (added) {
            instance.onChange(value, null, fromBackend)
        }
    }

}