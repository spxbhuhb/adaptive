package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel

class ServiceConnector(
    val thisHandle: AutoHandle,
    val peerHandle: AutoHandle,
    val service: AutoApi,
    val logger: AdaptiveLogger,
    val scope: CoroutineScope,
    pendingLimit: Int
) : AutoConnector(peerHandle.clientId) {

    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    init {
        scope.safeLaunch(logger) { run() }
    }

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)
        if (! result.isSuccess) throw DisconnectException("failed to send operation: $operation") // TODO proper disconnect/reconnect handling in ServiceConnector
    }

    suspend fun run() {
        for (operation in operations) {
            // TODO proper disconnect/reconnect handling in ServiceConnector
            when (operation) {
                is AutoAdd -> service.add(peerHandle, operation)
                is AutoModify -> service.modify(peerHandle, operation)
                is AutoMove -> service.move(peerHandle, operation)
                is AutoRemove -> service.remove(peerHandle, operation)
            }
        }
    }

    override suspend fun disconnect() {
        service.removePeer(thisHandle)
    }

    override fun onDisconnect() {
        safeCall(logger) {
            operations.close()
        }
    }

}