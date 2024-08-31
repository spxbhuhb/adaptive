package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.MetadataId
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.reflect.CallSiteName

class SetBackend(
    override val context: BackendContext
) : CollectionBackendBase(context.handle.clientId) {

    val additions = mutableSetOf<Pair<ItemId, MetadataId?>>()
    val removals = mutableSetOf<ItemId>()

    override val items = mutableMapOf<ItemId, PropertyBackend>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun add(item: AdatClass<*>, metadataId: MetadataId?, parentItemId: ItemId?, commit: Boolean, distribute: Boolean) {
        val itemId = context.nextTime()
        addItem(itemId, metadataId, item)

        val operation = AutoAdd(itemId, itemId, metadataId, parentItemId, encode(item.toArray()))
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

        addItem(operation.itemId, operation.metadataId, decode(operation.payload))

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
            trace { "SKIP SYNC: time= $time peerTime=$peerTime" }
            return
        }

        if (additions.isEmpty()) {
            connector.send(AutoEmpty(time))
        }

        if (removals.isNotEmpty()) {
            connector.send(AutoRemove(time, removals))
        }

        for (item in items.values) {
            val itemId = item.itemId

            if (itemId > peerTime) {
                connector.send(AutoAdd(itemId, itemId, item.metadataId, null, encode(item.values)))
            } else {
                item.syncPeer(connector, peerTime)
            }
        }

        trace { "SYNC DONE: time= $time peerTime=$peerTime" }
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

    fun addItem(itemId: ItemId, metadataId: ItemId?, value: AdatClass<*>) : PropertyBackend {
        additions += (itemId to metadataId)
        val backend = PropertyBackend(context, itemId, metadataId, value.toArray())
        items += itemId to backend
        return backend
    }

    private fun encode(values : Array<Any?>) =
        context.defaultWireFormat.wireFormatEncode(context.wireFormatProvider.encoder(), values).pack()

    private fun decode(payload : ByteArray) =
        context.defaultWireFormat.wireFormatDecode(context.wireFormatProvider.decoder(payload))

}