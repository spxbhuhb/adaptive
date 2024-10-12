package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.backend.tree.TreeData
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*
import kotlin.collections.minusAssign

class TreeBackend<A : AdatClass>(
    override val context: BackendContext<A>
) : CollectionBackendBase<A>(context.handle) {

    val tree = TreeData(this)
    override val items = mutableMapOf<ItemId, PropertyBackend<A>>()

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun remove(itemId: ItemId, commit: Boolean) {
        tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE)
        items -= itemId

        val operation = AutoRemove(context.nextTime(), true, setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. $operation" }

        close(operation, commit)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean) {

        for (itemId in itemIds) {
            tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE, commit = false)
            items -= itemId
        }

        tree.recomputeParentsAndChildren()

        val operation = AutoRemove(context.nextTime(), true, itemIds)
        trace { "FE -> BE  commit true .. $operation" }

        close(operation, commit)
    }

    override fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val item = items[itemId] ?: return
        item.modify(itemId, propertyName, propertyValue)
    }

    // --------------------------------------------------------------------------------
    // Operations from the tree data (correction after conflicting move)
    // --------------------------------------------------------------------------------

    fun moved(itemId : ItemId, parentId : ItemId) {

    }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun add(operation: AutoAdd, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        checkNotNull(operation.parentItemId) { "tree items must have a parent" }

        @Suppress("UNCHECKED_CAST")
        addItem(operation.itemId, operation.parentItemId, decode(operation.wireFormatName, operation.payload) as A)

        closeListOp(operation, setOf(operation.itemId), commit)
    }

    override fun remove(operation: AutoRemove, commit: Boolean) {
        trace { "commit=$commit op=$operation" }

        for (itemId in operation.itemIds) {
            tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE, commit = false)
            items -= itemId
        }

        tree.recomputeParentsAndChildren()

        closeListOp(operation, operation.itemIds, commit)
    }

    override fun modify(operation: AutoModify, commit: Boolean) {
        // FIXME check if the item has been removed
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
    }

    override fun syncEnd(operation: AutoSyncEnd, commit: Boolean) {
        afterSync.forEach { modify(it, commit) }
        afterSync.clear()
        context.receive(operation.timestamp)
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

//        val time = context.time
//
//        if (peerTime.timestamp >= time.timestamp) {
//            trace { "SKIP SYNC: time= $time peerTime=$peerTime" }
//            return
//        }
//
//        val removals = tree.removedNodes.children
//        if (removals.isNotEmpty()) {
//            connector.send(AutoRemove(peerTime, true, removals.map { it.id }.toSet()))
//        }
//
//        for (item in items.values) {
//            item.syncPeer(connector, peerTime)
//        }

    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId: ItemId?, value: A) {
        checkNotNull(parentItemId) { "tree items must have a parent" }
        tree.addChildToParent(itemId, parentItemId)

        val values = value.toArray()
        val backend = PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, values)

        items += itemId to backend

        context.onChange(value, null)
    }
}