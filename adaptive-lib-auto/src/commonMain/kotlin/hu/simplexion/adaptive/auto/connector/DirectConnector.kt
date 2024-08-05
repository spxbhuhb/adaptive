package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.utility.UUID

class DirectConnector(
    val peer: AutoBackend
) : AutoConnector() {

    override suspend fun add(globalId: UUID<AutoBackend>, operation: AutoAdd) {
        peer.receive(operation, false)
    }

    override suspend fun modify(globalId: UUID<AutoBackend>, operation: AutoModify) {
        peer.receive(operation, false)
    }

    override suspend fun move(globalId: UUID<AutoBackend>, operation: AutoMove) {
        peer.receive(operation, false)
    }

    override suspend fun remove(globalId: UUID<AutoBackend>, operation: AutoRemove) {
        peer.receive(operation, false)
    }

    override suspend fun transaction(globalId: UUID<AutoBackend>, operation: AutoTransaction) {
        peer.receive(operation, false)
    }

}