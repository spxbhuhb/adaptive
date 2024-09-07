package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val peer: BackendBase
) : AutoConnector(peer.peerId) {

    override fun send(operation: AutoOperation) {
        peer.receive(operation, false)
    }

    override suspend fun disconnect() {
        // nothing to do for direct connector
    }

    override fun onDisconnect() {
        // nothing to do for direct connector
    }

}