package `fun`.adaptive.auto.worker

import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.backend.BackendBase
import `fun`.adaptive.auto.connector.AutoConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class AutoWorker : WorkerImpl<AutoWorker> {

    val backendLock = getLock()

    val backends = mutableMapOf<AutoHandle, BackendBase>()

    override suspend fun run() {
        // worker is event-driven
    }

    operator fun get(handle: AutoHandle): BackendBase? =
        backendLock.use {
            backends[handle]
        }

    fun register(backend: BackendBase) {
        backendLock.use {
            backends[backend.context.handle] = backend
        }
    }

    fun deregister(backend: BackendBase) {
        backendLock.use {
            backends.remove(backend.context.handle)
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        backendLock.use {
            checkNotNull(backends[handle]) { "missing auto instance: $handle" }
        }
            .context.time

    fun addPeer(handle: AutoHandle, connector: AutoConnector, connectingPeerTime: LamportTimestamp): LamportTimestamp =
        backendLock.use {
            checkNotNull(backends[handle]) { "missing auto instance: $handle" }
        }
            .addPeer(connector, connectingPeerTime)

    fun receive(handle: AutoHandle, operation: AutoOperation) {
        backendLock.use {
            backends[handle]?.receive(operation, false)
        }
    }

}