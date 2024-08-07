package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.reflect.CallSiteName

class ListBackend(
    override val context: BackendContext
) : CollectionBackendBase() {

    val additions = mutableSetOf<ItemId>()
    val removals = mutableSetOf<ItemId>()

    override val items = mutableMapOf<ItemId, PropertyBackend>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun add(itemId: ItemId, parentItemId: ItemId?, commit: Boolean, distribute: Boolean) {
        additions += itemId
        addItem(itemId)
    }

    override fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean) {
        removals += itemId
        items -= itemId
    }

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = items[itemId] ?: return
        item.modify(itemId, propertyName, propertyValue)
    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun add(operation: AutoAdd, commit: Boolean, distribute: Boolean) {
        additions += operation.itemId
        closeListOp(operation, operation.itemId, commit, distribute)
    }

    override fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean) {
        removals += operation.itemId
        closeListOp(operation, operation.itemId, commit, distribute)
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

        // Send the additions and removals. This will modify only
        // the presence information of the items, not the items
        // themselves.

        val transaction = AutoTransaction(time, additions, removals)

        trace { "peerTime=$peerTime op=$transaction" }

        connector.send(transaction)
    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun transaction(transaction: AutoTransaction, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$transaction" }

        for (operation in transaction.modifications ?: emptyList()) {
            operation.apply(this, commit = false, distribute = false)
        }

        val trnAdditions = transaction.additions
        if (trnAdditions != null) {
            additions += trnAdditions
            for (itemId in trnAdditions) {
                addItem(itemId)
            }
        }

        val trnRemovals = transaction.removals
        if (trnRemovals != null) {
            removals += trnRemovals
            for (itemId in trnRemovals) {
                items -= itemId
            }
        }

        close(transaction, commit, distribute)
    }

    @CallSiteName
    fun closeListOp(operation: AutoOperation, itemId: ItemId, commit: Boolean, distribute: Boolean, callSiteName: String = "") {
        if (context.time < operation.timestamp) {
            context.receive(operation.timestamp)
            trace(callSiteName) { "BE -> BE  itemId=${itemId} .. commit $commit .. distribute $distribute .. $operation" }
            close(operation, commit, distribute)
        } else {
            trace(callSiteName) { "BE -> BE  SKIP  $operation" }
        }
    }

    fun addItem(itemId: ItemId) {
        items += itemId to PropertyBackend(context, itemId, TODO())
    }
}