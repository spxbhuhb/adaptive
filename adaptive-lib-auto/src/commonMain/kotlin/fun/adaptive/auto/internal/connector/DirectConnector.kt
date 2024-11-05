package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoOperation
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class DirectConnector(
    val instance: AutoGeneric,
    val connectingPeer: AutoGeneric
) : AutoConnector() {

    override val peerHandle: AutoHandle
        get() = connectingPeer.handle

    suspend fun run(connectingTime: LamportTimestamp) {
        instance.backend.syncPeer(this, connectingTime, null)
    }

    override fun send(operation: AutoOperation) {
        connectingPeer.backend.receive(operation)
    }

    override suspend fun disconnect() {
        connectingPeer.removeConnector(instance.handle)
        instance.removeConnector(connectingPeer.handle)
    }

    override fun dispose() {
        // nothing to do for direct connector
    }

}