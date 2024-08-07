package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.backend.BackendBase
import hu.simplexion.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val peer: BackendBase
) : AutoConnector() {

    override fun send(operation: AutoOperation) {
        peer.receive(operation, false)
    }

}