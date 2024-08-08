package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.encode
import hu.simplexion.adaptive.adat.toArray
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.MetadataId
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.reflect.CallSiteName

class ListBackend(
    override val context: BackendContext
) : CollectionBackendBase() {

    val additions = mutableSetOf<Pair<ItemId, MetadataId?>>()
    val removals = mutableSetOf<ItemId>()

    override val items = mutableMapOf<ItemId, PropertyBackend>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun add(item: AdatClass<*>, metadataId: MetadataId?, parentItemId: ItemId?, commit: Boolean, distribute: Boolean) {
        val itemId = context.nextTime()
        addItem(itemId, metadataId, item)

        val operation = AutoAdd(itemId, itemId, metadataId, parentItemId, item.encode(context.wireFormatProvider))
        trace { "FE -> BE  itemId=$itemId .. commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    override fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean) {
        removals += itemId
        items -= itemId

        val operation = AutoRemove(context.nextTime(), setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean) {
        removals += itemIds

        for (itemId in itemIds) {
            items -= itemId
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

        addItem(operation.itemId, operation.metadataId, context.wireFormatProvider.decode(operation.payload, context.defaultWireFormat))

        closeListOp(operation, setOf(operation.itemId), commit, distribute)
    }

    override fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        removals += operation.itemIds
        closeListOp(operation, operation.itemIds, commit, distribute)
    }

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
        if (operation.itemId in removals) return
        val item = items[operation.itemId] ?: return
        item.modify(operation, commit, distribute)
    }

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    override suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp) {
        val time = context.time

        if (peerTime.timestamp >= time.timestamp) {
            trace { "SKIP SYNC: time= $time peerTime=$peerTime" }
            return
        }

        // First send all the item values, the peer will create an item
        // on-the-fly if necessary, so it is safe to send whatever we
        // have right now. Item syncs are transactional, so one item
        // should be complete at any given time.

        for (item in items.values) {
            item.syncPeer(connector, peerTime)
        }

    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    @CallSiteName
    fun closeListOp(operation: AutoOperation, itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean, callSiteName: String = "") {
        if (context.time < operation.timestamp) {
            context.receive(operation.timestamp)
            trace(callSiteName) { "BE -> BE  itemIds=${itemIds} .. commit $commit .. distribute $distribute .. $operation" }
            close(operation, commit, distribute)
        } else {
            trace(callSiteName) { "BE -> BE  SKIP  $operation" }
        }
    }

    fun addItem(itemId: ItemId, metadataId: ItemId?, value: AdatClass<*>) {
        additions += (itemId to metadataId)
        items += itemId to PropertyBackend(context, itemId, metadataId, value.toArray())
    }
}