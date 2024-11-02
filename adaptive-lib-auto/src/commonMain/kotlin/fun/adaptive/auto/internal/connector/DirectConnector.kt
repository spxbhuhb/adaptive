package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val instance : AutoGeneric,
    val peer: AutoGeneric,
) : AutoConnector() {

    override val peerHandle: AutoHandle
        get() = peer.handle

    override fun send(operation: AutoOperation) {
        peer.receive(operation)
    }

    override suspend fun disconnect() {
        peer.removePeer(instance.handle)
        instance.removePeer(peer.handle)
    }

    override fun dispose() {
        // nothing to do for direct connector
    }

}