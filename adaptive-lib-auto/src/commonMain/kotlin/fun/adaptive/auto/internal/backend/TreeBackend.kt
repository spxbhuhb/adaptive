package `fun`.adaptive.auto.internal.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.encode
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.backend.tree.TreeData
import `fun`.adaptive.auto.internal.connector.AutoConnector
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.MetadataId
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.reflect.CallSiteName
import kotlin.collections.minusAssign

class TreeBackend(
    override val context: BackendContext
) : CollectionBackendBase(context.handle.clientId) {

    val tree = TreeData(this)
    override val items = mutableMapOf<ItemId, PropertyBackend>()

    override val defaultWireFormatName = context.defaultWireFormat.wireFormatName

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    override fun remove(itemId: ItemId, commit: Boolean, distribute: Boolean) {
        tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE)
        items -= itemId

        val operation = AutoRemove(context.nextTime(), setOf(itemId))
        trace { "FE -> BE  itemId=$itemId .. commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
    }

    override fun removeAll(itemIds: Set<ItemId>, commit: Boolean, distribute: Boolean) {

        for (itemId in itemIds) {
            tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE, commit = false)
            items -= itemId
        }

        tree.recomputeParentsAndChildren()

        val operation = AutoRemove(context.nextTime(), itemIds)
        trace { "FE -> BE  commit true .. distribute true .. $operation" }

        close(operation, commit, distribute)
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

    override fun add(operation: AutoAdd, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        checkNotNull(operation.parentItemId) { "tree items must have a parent" }

        addItem(operation.itemId, operation.parentItemId, context.wireFormatProvider.decode(operation.payload, context.defaultWireFormat))

        closeListOp(operation, setOf(operation.itemId), commit, distribute)
    }

    override fun remove(operation: AutoRemove, commit: Boolean, distribute: Boolean) {
        trace { "commit=$commit distribute=$distribute op=$operation" }

        for (itemId in operation.itemIds) {
            tree.afterApply(itemId, tree.removedNodes.id, Int.MAX_VALUE, commit = false)
            items -= itemId
        }

        tree.recomputeParentsAndChildren()

        closeListOp(operation, operation.itemIds, commit, distribute)
    }

    override fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean) {
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

        val removals = tree.removedNodes.children
        if (removals.isNotEmpty()) {
            connector.send(AutoRemove(peerTime, removals.map { it.id }.toSet()))
        }

        for (item in items.values) {
            item.syncPeer(connector, peerTime)
        }

    }

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    override fun addItem(itemId: ItemId, parentItemId : ItemId?, value: AdatClass<*>) {
        checkNotNull(parentItemId) { "tree items must have a parent" }
        tree.addChildToParent(itemId, parentItemId)
        items += itemId to PropertyBackend(context, itemId, value.adatCompanion.wireFormatName, value.toArray())
    }
}