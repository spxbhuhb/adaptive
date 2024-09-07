package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.model.PeerId
import `fun`.adaptive.auto.model.operation.AutoOperation

abstract class AutoConnector(
    val peerId: PeerId
) {

    abstract fun send(operation: AutoOperation)

    /**
     * Disconnect this connector from its peer. Calls `AutoApi.removePeer` to
     * notify the peer. `AutoApi.removePeer` in turn calls [onDisconnect] of the
     * peer connector.
     */
    abstract suspend fun disconnect()

    /**
     * Called when a peer of this connector disconnects.
     */
    abstract fun onDisconnect()


}