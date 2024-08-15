package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val peer: BackendBase
) : AutoConnector() {

    override fun send(operation: AutoOperation) {
        peer.receive(operation, false)
    }

}