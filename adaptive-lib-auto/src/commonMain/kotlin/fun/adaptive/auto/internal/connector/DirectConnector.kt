package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val connecting : BackendBase,
    val peer: BackendBase
) : AutoConnector() {

    override fun send(operation: AutoOperation) {
        peer.receive(operation)
    }

    override suspend fun disconnect() {
        peer.removePeer(connecting.context.handle)
    }

    override fun dispose() {
        // nothing to do for direct connector
    }

}