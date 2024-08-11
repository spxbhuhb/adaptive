package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.connector.AutoConnector
import `fun`.adaptive.auto.frontend.FrontendBase
import `fun`.adaptive.auto.model.operation.AutoModify
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.reflect.CallSiteName
import kotlinx.coroutines.launch

abstract class BackendBase : AutoConnector() {

    abstract val context: BackendContext

    var frontEnd: FrontendBase? = null

    // --------------------------------------------------------------------------------
    // Operations from the frontend
    // --------------------------------------------------------------------------------

    abstract fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?)

    // --------------------------------------------------------------------------------
    // Operations from peers
    // --------------------------------------------------------------------------------

    override fun send(operation: AutoOperation) {
        //  When the backend acts as connector, send simply has to call receive.
        receive(operation, false)
    }

    fun receive(operation: AutoOperation, distribute: Boolean) {
        trace { "distribute=$distribute op=$operation" }

        operation.apply(this, commit = true, distribute)

        context.receive(operation.timestamp)
    }

    abstract fun modify(operation: AutoModify, commit: Boolean, distribute: Boolean)

    // --------------------------------------------------------------------------------
    // Peer synchronization
    // --------------------------------------------------------------------------------

    /**
     * Add a connection to a peer. The backend launches [syncPeer] in to send all known
     * operations that happened after the [peerTime] to the peer.
     */
    fun addPeer(connector: AutoConnector, peerTime: LamportTimestamp): LamportTimestamp {
        context.addConnector(connector)
        context.scope.launch { syncPeer(connector, peerTime) }
        return context.time
    }

    abstract suspend fun syncPeer(connector: AutoConnector, peerTime: LamportTimestamp)

    // --------------------------------------------------------------------------------
    // Utility, common
    // --------------------------------------------------------------------------------

    fun close(operation: AutoOperation, commit: Boolean, distribute: Boolean) {

        if (commit) {
            frontEnd?.commit()
            trace { "==== commit ====\n" }
        }

        if (distribute) {
            for (connector in context.connectors) {
                connector.send(operation)
            }
        }

    }

    @CallSiteName
    inline fun trace(callSiteName: String = "<unknown>", builder: () -> String) {
        if (context.trace) trace(builder(), callSiteName)
    }

    open fun trace(message: String, callSiteName: String) {
        println("[${callSiteName.substringAfterLast('.')} @ ${context.time}] $message")
    }
}