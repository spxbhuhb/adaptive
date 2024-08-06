package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.api.AutoApi
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.service.model.DisconnectException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ServiceConnector(
    val peerHandle: AutoHandle,
    val service: AutoApi,
    scope: CoroutineScope,
    pendingLimit: Int
) : AutoConnector() {


    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    init {
        scope.launch { run() }
    }

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)
        if (! result.isSuccess) throw DisconnectException() // TODO proper disconnect/reconnect handling in ServiceConnector
    }

    suspend fun run() {
        for (operation in operations) {
            // TODO proper disconnect/reconnect handling in ServiceConnector
            when (operation) {
                is AutoAdd -> service.add(peerHandle, operation)
                is AutoModify -> service.modify(peerHandle, operation)
                is AutoMove -> service.move(peerHandle, operation)
                is AutoRemove -> service.remove(peerHandle, operation)
                is AutoTransaction -> service.transaction(peerHandle, operation)
                is AutoSyncEnd -> service.syncEnd(peerHandle, operation)
            }
        }
    }

}