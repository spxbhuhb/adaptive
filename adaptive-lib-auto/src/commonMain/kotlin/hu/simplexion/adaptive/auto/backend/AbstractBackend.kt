package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.operation.AutoModify
import hu.simplexion.adaptive.auto.model.operation.AutoOperation
import hu.simplexion.adaptive.auto.model.operation.AutoTransaction
import hu.simplexion.adaptive.foundation.unsupported
import hu.simplexion.adaptive.reflect.CallSiteName
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import kotlinx.coroutines.launch

abstract class AbstractBackend : AutoConnector() {

    abstract val context: BackendContext

    val connectorLock = getLock()

    private var pConnectors = listOf<AutoConnector>()

    val connectors
        get() = connectorLock.use { pConnectors }

    /**
     * Add a connection to a peer. The backend launches [syncPeer] in to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {
        connectorLock.use { pConnectors += connector }
        context.scope.launch { syncPeer(connector, peerTime) }
        return context.time
    }

    abstract suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp)

    /**
     * Called by the connector as the last operation of a peer synchronization.
     */
    abstract fun syncEnd(peerTime: LamportTimestamp)

    /**
     * Receive the operation from the peer. Intended to be implemented by
     * backends, calls [AutoOperation.apply].
     */
    abstract fun receive(operation: AutoOperation, distribute: Boolean)

    /**
     * Process all operations in the transaction at once.
     */
    abstract fun transaction(transaction: AutoTransaction, commit: Boolean, distribute: Boolean)

    /**
     * Modify a property value.
     *
     * Backends: instance, list, tree
     */
    abstract fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean)

    /**
     * Insert an item.
     *
     * Backends: list
     */
    open fun insert(timestamp: LamportTimestamp, item: ItemId, origin: ItemId, left: ItemId, right: ItemId) {
        unsupported()
    }

    /**
     * Add a child to a parent.
     *
     * Backends: tree
     */
    open fun add(timestamp: LamportTimestamp, item: ItemId, parent: ItemId) {
        unsupported()
    }

    /**
     * Move a child from one parent to another.
     *
     * Backends: tree
     */
    open fun move(timestamp: LamportTimestamp, item: ItemId, newParent: ItemId) {
        unsupported()
    }

    /**
     * Remove an item.
     *
     * Backends: list, tree
     */
    open fun remove(timestamp: LamportTimestamp, item: ItemId) {
        unsupported()
    }

    @CallSiteName
    inline fun trace(callSiteName: String = "<unknown>", builder: () -> String) {
        if (context.trace) trace(builder(), callSiteName)
    }

    open fun trace(message: String, callSiteName: String) {
        println("[${callSiteName.substringAfterLast('.')} @ ${context.time}] $message")
    }
}