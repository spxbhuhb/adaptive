package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoOperation

class DirectConnector(
    val instance: AutoGeneric,
    val connectingPeer: AutoGeneric
) : AutoConnector() {

    override val peerHandle: AutoHandle
        get() = connectingPeer.handle

    suspend fun run(connectingTime: LamportTimestamp) {
        instance.syncPeer(this, connectingTime)
    }

    override fun send(operation: AutoOperation) {
        connectingPeer.remoteReceive(operation)
    }

    override suspend fun disconnect() {
        connectingPeer.removeConnector(instance.handle)
        instance.removeConnector(connectingPeer.handle)
    }

    override fun dispose() {
        // TODO should direct connector have dispose (sync coroutine stop maybe)
    }

}