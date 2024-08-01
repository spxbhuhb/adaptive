package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.operation.AutoOperation

class DirectConnector(
    val peer: AutoBackend
) : AutoConnector() {

    override fun send(operation: AutoOperation, distribute: Boolean) {
        peer.receive(operation, distribute)
    }

}