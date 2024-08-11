package `fun`.adaptive.auto.connector

import `fun`.adaptive.auto.backend.BackendBase
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val peer: BackendBase
) : AutoConnector() {

    override fun send(operation: AutoOperation) {
        peer.receive(operation, false)
    }

}