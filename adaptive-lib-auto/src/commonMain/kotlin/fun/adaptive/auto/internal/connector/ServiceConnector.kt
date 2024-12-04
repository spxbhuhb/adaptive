package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.utility.ThreadSafe
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.untilSuccess
import `fun`.adaptive.utility.use
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.coroutineContext

class ServiceConnector(
    val instance: AutoInstance<*, *, *, *>,
    val service: AutoApi,
    override val peerHandle: AutoHandle,
    val initiator: Boolean,
    pendingLimit: Int = 10000,
) : AutoConnector() {

    val lock = getLock()

    enum class Status {
        CREATED,
        CONNECTING,
        CONNECTED,
        DISPOSING,
        DISPOSED
    }

    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    @ThreadSafe
    var status = Status.CREATED
        get() = lock.use { field }
        private set(value) = lock.use { field = value }

    var operationToSend: AutoOperation? = null

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)

        // This happens when the channel is full. With a limit of 10000 this probably
        // means that the peer disconnected. `removePeer` calls `dispose` which
        // stops `run` and closes `operations`.

        // FIXME remove peer
        if (! result.isSuccess) {
            instance.removeConnector(peerHandle)
        }
    }

    suspend fun run() {
        while (coroutineContext.isActive) {
            safeSuspendCall(instance.logger) {

                connect() // tries until successful or disposed

                try {
                    sendOperations()
                } catch (ex: Exception) {
                    instance.error("service connection error", ex)
                    disconnect()
                }
            }
            if (! initiator) break
        }
    }

    /**
     * Connect to the other peer. Can be used on both peers.
     */
    suspend fun connect() {
        check(status in listOf(Status.CREATED, Status.CONNECTING))

        status = Status.CONNECTING

        val peerTime = untilSuccess {

            if (status != Status.CONNECTING) {
                return@untilSuccess null // the service connector has been disposed
            }

            instance.logger.fine { "CONNECTING :: ${instance.remoteHandle}" }

            if (initiator) {
                // starts a synchronization from the remote to this
                service.addPeer(
                    instance.remoteHandle,
                    instance.handle,
                    instance.time
                )
            }

            service.peerTime(instance.remoteHandle)
        }

        if (peerTime == null) return // he service connector has been disposed

        instance.logger.fine { "CONNECTED :: ${instance.remoteHandle}" }

        instance.scope.launch {
            supervisorScope {
                launch { instance.syncPeer(this@ServiceConnector, peerTime) }
            }
        }

        status = Status.CONNECTED
    }

    /**
     * Returns when:
     *
     * - `receive` is interrupted by a job cancel. This means dispose.
     * - `sendOperation` throws an exception. This means connection failure.
     */
    suspend fun sendOperations() {
        while (status == Status.CONNECTED) {

            val toSend = operationToSend ?: operations.receive()
            operationToSend = toSend

            sendOperation(toSend)

            operationToSend = null
        }
    }

    suspend fun sendOperation(operation: AutoOperation) {
        when (operation) {
            is AutoAdd -> service.add(peerHandle, operation)
            is AutoUpdate -> service.update(peerHandle, operation)
            is AutoMove -> service.move(peerHandle, operation)
            is AutoRemove -> service.remove(peerHandle, operation)
            is AutoEmpty -> service.empty(peerHandle, operation)
            is AutoSyncBatch -> service.syncBatch(peerHandle, operation)
            is AutoSyncEnd -> service.syncEnd(peerHandle, operation)
        }
    }

    override suspend fun disconnect() {
//        safeSuspendCall(logger) {
//
//            status = if (reconnect) Status.CONNECTING else Status.DISPOSING
//
//            // removing the peer means that we won't get any new operations
//            instance.removeConnector(peerHandle)
//            peerHandleOrNull = null
//
//            // empty the list of operations, we are disconnected, no need for them
//            // this is needed only if we are planning to reconnect
//
//            while (reconnect) {
//                if (operations.tryReceive().isFailure) break
//            }
//
//            if (!reconnect) {
//                dispose()
//            }
//        }
    }

    //
    override fun dispose() {
//        safeCall(logger, message = "onDisconnect error in ServiceConnector @ $instance") {
//            status = Status.DISPOSING
//
//            operations.close()
//            job?.cancel()
//
//            status = Status.DISPOSED
//        }
    }

}