package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.AutoOperation

abstract class AutoConnector {

    abstract val peerHandle: AutoHandle

    abstract fun send(operation: AutoOperation)

    /**
     * Disconnect this connector from its peer. Calls `AutoApi.removePeer` to
     * notify the peer. `AutoApi.removePeer` in turn calls [dispose] of the
     * peer connector.
     */
    abstract suspend fun disconnect()

    /**
     * Called when the backend removes this connector with `removeConnector` and
     * the connector will no longer be used. The connector has to free all resources
     * it holds.
     */
    abstract fun dispose()

}