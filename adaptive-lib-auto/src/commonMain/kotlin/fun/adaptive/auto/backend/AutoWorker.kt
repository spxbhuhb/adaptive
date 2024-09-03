package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class AutoWorker : WorkerImpl<AutoWorker> {

    val backendLock = getLock()

    val backends = mutableMapOf<UUID<BackendBase>, BackendBase>()

    override suspend fun run() {
        // worker is event-driven
    }

    operator fun get(handle: AutoHandle): BackendBase? =
        backendLock.use {
            backends[handle.globalId]
        }

    fun register(backend: BackendBase) {
        backendLock.use {
            backends[backend.globalId] = backend
        }
    }

    fun deregister(backend: BackendBase) {
        backendLock.use {
            backends.remove(backend.globalId)
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        backendLock.use {
            checkNotNull(backends[handle.globalId]) { "missing auto instance: $handle" }
        }
            .context.time

    fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingPeerTime: LamportTimestamp, transport: ServiceCallTransport): LamportTimestamp =
        backendLock.use {
            checkNotNull(backends[origin.globalId]) { "missing auto instance: $origin" }
        }.let {
            it.addPeer(
                ServiceConnector(origin, connecting, getService(transport), it.context.logger, scope, 1000),
                connectingPeerTime
            )
        }

    fun removePeer(handle: AutoHandle) =
        backendLock.use {
            checkNotNull(backends[handle.globalId]) { "missing auto instance: $handle" }
        }.also {
            it.removePeer(handle)
        }

    fun receive(handle: AutoHandle, operation: AutoOperation) {
        backendLock.use {
            backends[handle.globalId]?.receive(operation, false)
        }
    }

}