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

    val instanceLock = getLock()

    val instances = mutableMapOf<AutoHandle, BackendBase>()

    override suspend fun run() {
        // worker is event-driven
    }

    operator fun get(handle: AutoHandle): BackendBase? =
        instanceLock.use {
            instances[handle]
        }

    fun register(backend: BackendBase) {
        instanceLock.use {
            instances[backend.context.handle] = backend
        }
    }

    fun unregister(backend: BackendBase) {
        instanceLock.use {
            instances.remove(backend.context.handle)
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle]) { "missing auto instance: $handle" }
        }
            .context.time

    fun addPeer(handle: AutoHandle, connector: AutoConnector, connectingPeerTime: LamportTimestamp): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle]) { "missing auto instance: $handle" }
        }
            .addPeer(connector, connectingPeerTime)

    fun receive(handle: AutoHandle, operation: AutoOperation) {
        instanceLock.use {
            instances[handle]?.receive(operation, false)
        }
    }

}