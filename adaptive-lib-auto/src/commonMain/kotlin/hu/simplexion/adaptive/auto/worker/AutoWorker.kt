package hu.simplexion.adaptive.auto.worker

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.AutoOperation
import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use

class AutoWorker : WorkerImpl<AutoWorker> {

    val instanceLock = getLock()

    val instances = mutableMapOf<AutoHandle, AutoBackend>()

    override suspend fun run() {
        // worker is event-driven
    }

    operator fun get(handle: AutoHandle): AutoBackend? =
        instanceLock.use {
            instances[handle]
        }

    fun register(backend: AutoBackend) {
        instanceLock.use {
            instances[backend.handle] = backend
        }
    }

    fun unregister(backend: AutoBackend) {
        instanceLock.use {
            instances.remove(backend.handle)
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle]) { "missing auto instance: $handle" }
        }
            .time

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