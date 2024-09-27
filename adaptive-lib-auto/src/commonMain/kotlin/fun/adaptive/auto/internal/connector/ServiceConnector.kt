package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeLaunch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive

class ServiceConnector(
    val thisHandle: AutoHandle,
    peerHandle: AutoHandle,
    val service: AutoApi,
    val logger: AdaptiveLogger,
    val scope: CoroutineScope,
    pendingLimit: Int
) : AutoConnector(peerHandle) {

    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    val job = scope.safeLaunch(logger, "run error in ServiceConnector @ $thisHandle") { run() }

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)
        if (! result.isSuccess) throw DisconnectException("failed to send operation: $operation") // TODO proper disconnect/reconnect handling in ServiceConnector
    }

    suspend fun run() {
        try {
            for (operation in operations) {
                // TODO proper disconnect/reconnect handling in ServiceConnector
                when (operation) {
                    is AutoAdd -> service.add(peerHandle, operation)
                    is AutoModify -> service.modify(peerHandle, operation)
                    is AutoMove -> service.move(peerHandle, operation)
                    is AutoRemove -> service.remove(peerHandle, operation)
                    is AutoEmpty -> service.empty(peerHandle, operation)
                    is AutoSyncEnd -> service.syncEnd(peerHandle, operation)
                }
            }
        } catch (ex : Exception) {

            job.ensureActive()

            // FIXME I don't think this is the right way to handle the cancellation of the service out channel

            if (ex !is CancellationException) {
                throw ex
            }

            // CancellationException here means that the service call has thrown that exception
            // this is NOT a cancellation of `run` (that has been handled by `ensureActive`)
            // but some other cancellation exception (most notably, Netty's closed connection)
        }
    }

    override suspend fun disconnect() {
        service.removePeer(thisHandle)
    }

    override fun onDisconnect() {
        safeCall(logger, "onDisconnect error in ServiceConnector @ $thisHandle") {
            job.cancel()
            operations.close()
        }
    }

}