package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.backend.AbstractBackend
import hu.simplexion.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val peer: AbstractBackend
) : AutoConnector() {

    override fun send(operation: AutoOperation) {
        peer.receive(operation, false)
    }

}