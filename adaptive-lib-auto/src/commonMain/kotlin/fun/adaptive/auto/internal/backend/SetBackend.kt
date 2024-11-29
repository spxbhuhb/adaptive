package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoAdd
import `fun`.adaptive.auto.model.operation.AutoEmpty
import `fun`.adaptive.auto.model.operation.AutoRemove
import `fun`.adaptive.auto.model.operation.AutoSyncBatch
import `fun`.adaptive.auto.model.operation.AutoSyncEnd
import `fun`.adaptive.auto.model.operation.AutoUpdate

class SetBackend<IT : AdatClass>(
    instance: AutoInstance<*, *, Collection<IT>, IT>,
    initialValue: MutableMap<ItemId, PropertyBackend<IT>>? = null,
) : AutoCollectionBackend<IT>(instance) {

    private val data = SetBackendData<IT>(initialValue)

    var lastUpdate: LamportTimestamp = LamportTimestamp.CONNECTING
        private set

    init {
        if (initialValue != null && initialValue.isNotEmpty()) {
            data.addAll(initialValue.keys)
            lastUpdate = initialValue.values.maxOf { it.lastUpdate }
        }
    }

    // --------------------------------------------------------------------------------
    // Operations initiated locally (through this instance)
    // --------------------------------------------------------------------------------

    override fun localAdd(timestamp: LamportTimestamp, item: IT, parentItemId: ItemId?): Pair<AutoAdd, IT> {
        val itemId = timestamp
        val item = addItem(itemId, item) !! // there is always a new item when adding locally

        val operation = AutoAdd(
            timestamp = timestamp,
            itemId = itemId,
            item.wireFormat.wireFormatName,
            parentItemId,
            item.encode()
        )

        return operation to item.getItem()
    }

    override fun localUpdate(
        timestamp: LamportTimestamp,
        itemId: ItemId,
        updates: Collection<Pair<String, Any?>>,
    ): Pair<AutoUpdate, IT> {
        val itemBackend = checkNotNull(data[itemId]) { "no item $itemId, maybe removed during the update" }
        return itemBackend.localUpdate(timestamp, itemId, updates)
    }

    override fun localRemove(timestamp: LamportTimestamp, itemId: ItemId): Pair<AutoRemove, IT?> {
        val removed = data.remove(itemId, fromPeer = false)
        val operation = AutoRemove(timestamp, true, setOf(itemId))
        return operation to removed
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun remoteAdd(operation: AutoAdd): Pair<LamportTimestamp, IT>? {

        val wireFormat = wireFormatFor(operation.wireFormatName)

        // TODO optimize, it is inefficient to encode into an instance and then convert it into an array
        @Suppress("UNCHECKED_CAST")
        val value = wireFormat.wireFormatDecode(instance.wireFormatProvider.decoder(operation.payload)) as IT

        val itemBackend = addItem(operation.itemId, value)

        if (itemBackend == null) {
            return null
        } else {
            return operation.timestamp to itemBackend.getItem()
        }
    }

    override fun remoteUpdate(operation: AutoUpdate): Triple<LamportTimestamp, IT?, IT>? {
        if (operation.itemId !in data) return null

        val item = data[operation.itemId]

        if (item != null) {
            return item.remoteUpdate(operation)
        }

        // This is a tricky situation, we've got a modification, but we don't have the
        // item yet. May happen if the item is updated during synchronization. In this
        // case we have to put this operation to the shelf until the synchronization
        // is done.

        trace { "POSTPONED :: $operation" }
        afterSync += operation

        return null
    }

    override fun remoteRemove(operation: AutoRemove): Pair<LamportTimestamp, Set<Pair<ItemId, IT>>>? {
        return operation.timestamp to data.remove(operation.itemIds, fromPeer = true)
    }

    override fun remoteSyncEnd(operation: AutoSyncEnd) {
        // apply modifications that have been postponed because we haven't had the
        // item at the time (see syncPeer and modify for details)
        afterSync.forEach { remoteUpdate(it) } // calls instance.commit and distribute
        afterSync.clear()
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    override suspend fun syncPeer(
        connector: AutoConnector,
        syncFrom: LamportTimestamp,
        syncBatch: MutableList<AutoUpdate>?,
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
        val modify = mutableListOf<AutoUpdate>()

        for (item in data.items()) {
            val itemId = item.itemId

            if (itemId.timestamp > syncFrom.timestamp) {
                add += AutoAdd(time, itemId, item.wireFormatName, null, item.encode())
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

    fun addItem(
        itemId: ItemId,
        value: IT
    ): AutoItemBackend<IT>? {

        val values = value.toArray()

        val backend = PropertyBackend<IT>(instance, value.adatCompanion.wireFormatName, values, null, itemId = itemId)

        val added = data.add(itemId, backend)

        return if (added) backend else null
    }

    override fun firstOrNull(filterFun: (IT) -> Boolean): AutoItemBackend<IT>? {
        for (itemBackend in data.items()) {
            val item = itemBackend.getItem()
            if (filterFun(item)) return itemBackend
        }
        return null
    }

    override fun filter(filterFun: (IT) -> Boolean): Collection<IT> =
        data.items().map { it.getItem() }.filter { filterFun(it) }

    override fun getItems(): Collection<IT> {
        return data.items().map { it.getItem() }
    }

    override fun getItem(itemId: ItemId): IT? {
        return data[itemId]?.getItem()
    }

    override fun export(withItems: Boolean): AutoCollectionExport<IT> =
        AutoCollectionExport(
            AutoMetadata(instance.connectionInfo, null, null), // FIXME removed items and milestone in auto collection export
            if (withItems) {
                data.items().map {
                    AutoItemExport<IT>(
                        null,
                        it.itemId,
                        it.propertyTimes.toList(),
                        it.getItem()
                    )
                }
            } else {
                emptyList()
            }
        )

    override fun exportItem(itemId: ItemId): AutoItemExport<IT>? =
        data[itemId]?.export(withMeta = false)

}