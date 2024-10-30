package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.untilSuccess
import `fun`.adaptive.utility.use
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

/**
 * @param  infoFun    The function that can get the connection info needed for connecting.
 *
 * @param  initiator  True when this backend is the initiator of the connection, false otherwise.
 *
 * @param  reconnect  When true the service connector calls `thisBackend.reconnect` in case of
 *                    communication error.
 */
class ServiceConnector(
    val instance: AutoInstance<*, *, *, *>,
    val service: AutoApi,
    val handle: AutoHandle?,
    val infoFun: suspend () -> AutoConnectionInfo<*>,
    val initiator: Boolean,
    val reconnect: Boolean,
    pendingLimit: Int = 1000,
) : AutoConnector() {

    val lock = getLock()

    enum class Status {
        CREATED,
        CONNECTING,
        CONNECTED,
        DISPOSING,
        DISPOSED
    }

    var job: Job? = null

    val logger = getLogger("$instance.connector")

    val operations: Channel<AutoOperation> = Channel(pendingLimit)

    var status = Status.CREATED
        get() = lock.use { field }
        private set(value) = lock.use { field = value }

    var operationToSend: AutoOperation? = null

    override val peerHandle: AutoHandle
        get() = checkNotNull(if (initiator) connectInfo?.acceptingHandle else connectInfo?.connectingHandle) {}

    var connectInfo: AutoConnectionInfo<*>? = null

    override fun send(operation: AutoOperation) {
        val result = operations.trySend(operation)

        // This happens when the channel is full. With a limit of 1000 this probably
        // means that the peer disconnected. `removePeer` calls `dispose` which
        // stops `run` and closes `operations`.

        // FIXME remove peer
        if (! result.isSuccess) {
            instance.removePeer(peerHandle)
        }
    }

    suspend fun run() {
        safeSuspendCall(logger) {

            while (coroutineContext.isActive) {

                connect() // tries until successful or disposed

                try {
                    sendOperations()
                } catch (ex: Exception) {
                    disconnect()
                }

                if (!reconnect) break
            }
        }
    }

    /**
     * Connect to the other peer. Can be used on both peers.
     */
    suspend fun connect() {
        check(status in listOf(Status.CREATED, Status.CONNECTING))

        status = Status.CONNECTING
        connectInfo = null

        // tries util succeeds or disposed
        val localConnectInfo = untilSuccess {

            if (status != Status.CONNECTING) {
                return@untilSuccess null // the service connector has been disposed
            }

            val info = infoFun()

            if (initiator) {
                // starts a synchronization from the remote to this
                service.addPeer(
                    info.acceptingHandle,
                    info.connectingHandle,
                    instance.time
                )
            }

            info
        }

        if (localConnectInfo == null) return // he service connector has been disposed

        connectInfo = localConnectInfo

        // starts a synchronization from this to the remote
        instance.addPeer(
            this,
            localConnectInfo.acceptingTime
        )

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
            is AutoModify -> service.modify(peerHandle, operation)
            is AutoMove -> service.move(peerHandle, operation)
            is AutoRemove -> service.remove(peerHandle, operation)
            is AutoEmpty -> service.empty(peerHandle, operation)
            is AutoSyncBatch -> service.syncBatch(peerHandle, operation)
            is AutoSyncEnd -> service.syncEnd(peerHandle, operation)
        }
    }

    override suspend fun disconnect() {
        safeSuspendCall(logger) {

            status = if (reconnect) Status.CONNECTING else Status.DISPOSING

            // removing the peer means that we won't get any new operations
            instance.removePeer(peerHandle)
            peerHandleOrNull = null

            // empty the list of operations, we are disconnected, no need for them
            // this is needed only if we are planning to reconnect

            while (reconnect) {
                if (operations.tryReceive().isFailure) break
            }

            if (!reconnect) {
                dispose()
            }
        }
    }

    override fun dispose() {
        safeCall(logger, message = "onDisconnect error in ServiceConnector @ $instance") {
            status = Status.DISPOSING

            operations.close()
            job?.cancel()

            status = Status.DISPOSED
        }
    }

}