package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeLaunch
import `fun`.adaptive.utility.safeSuspendCall
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * @param  reconnect  When true the service connector calls `thisBackend.reconnect` in case of
 *                    communication error.
 */
class ServiceConnector(
    val thisBackend: BackendBase,
    peerHandle: AutoHandle,
    val service: AutoApi,
    val reconnect: Boolean,
    pendingLimit: Int = 1000
) : AutoConnector(peerHandle) {

    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    val logger
        get() = thisBackend.context.logger

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)

        // This happens when the channel is full. With a limit of 1000 this probably
        // means that the peer disconnected. `removePeer` calls `dispose` which
        // stops `run` and closes `operations`.

        if (! result.isSuccess) thisBackend.removePeer(peerHandle)
    }

    suspend fun run() {
        try {
            for (operation in operations) {
                when (operation) {
                    is AutoAdd -> service.add(peerHandle, operation)
                    is AutoModify -> service.modify(peerHandle, operation)
                    is AutoMove -> service.move(peerHandle, operation)
                    is AutoRemove -> service.remove(peerHandle, operation)
                    is AutoEmpty -> service.empty(peerHandle, operation)
                    is AutoSyncBatch -> service.syncBatch(peerHandle, operation)
                    is AutoSyncEnd -> service.syncEnd(peerHandle, operation)
                }
            }
        } catch (ex: Exception) {

            safeSuspendCall(logger) {
                thisBackend.removePeer(peerHandle)
                if (reconnect) thisBackend.reconnect()
            }

            coroutineContext.ensureActive()

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
        safeSuspendCall(logger) {
            service.removePeer(thisBackend.peerHandle)
        }
    }

    override fun dispose() {
        safeCall(logger, message = "onDisconnect error in ServiceConnector @ $thisBackend") {
            operations.close()
        }
    }

}