package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.launch

class AutoWorker : WorkerImpl<AutoWorker> {

    val instanceLock = getLock()

    val instances = mutableMapOf<UUID<AutoGeneric>, AutoGeneric>()

    override suspend fun run() {
        // worker is event-driven
    }

    operator fun get(handle: AutoHandle): AutoGeneric? =
        instanceLock.use {
            instances[handle.globalId]
        }

    fun register(backend: AutoGeneric) {
        instanceLock.use {
            instances[backend.globalId] = backend
        }
    }

    fun deregister(backend: AutoGeneric) {
        instanceLock.use {
            instances.remove(backend.globalId)
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle.globalId]) { "missing auto instance: $handle" }
        }
            .context.time

    suspend fun addPeer(
        origin: AutoHandle,
        connecting: AutoHandle,
        connectingPeerTime: LamportTimestamp,
        transport: ServiceCallTransport
    ): LamportTimestamp {
        val backend = instanceLock.use {
            checkNotNull(instances[origin.globalId]) { "missing auto instance: $origin" }
        }

        val connector = ServiceConnector(backend, connecting, getService(transport), reconnect = false)

        val time = backend.addPeer(
            connector,
            connectingPeerTime
        )

        scope.launch { connector.run() }

        return time
    }

    fun removePeer(handle: AutoHandle) =
        instanceLock.use {
            // Decided not to throw an exception here as this is a pretty normal occurrence after
            // server start when the connected peers try to disconnect from with the old handle.
            // TODO should we try and persist handles when it's possible?
            instances[handle.globalId]?.removePeer(handle)
        }

    fun receive(handle: AutoHandle, operation: AutoOperation) {
        instanceLock.use {
            instances[handle.globalId]?.receive(operation)
        }
    }

}