package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val connecting: AutoBackend,
    val peer: AutoBackend,
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