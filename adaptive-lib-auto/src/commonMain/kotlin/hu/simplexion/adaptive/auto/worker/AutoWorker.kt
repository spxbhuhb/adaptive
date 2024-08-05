package hu.simplexion.adaptive.auto.worker

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.connector.AutoConnector
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.AutoOperation
import hu.simplexion.adaptive.server.builtin.WorkerImpl
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use

class AutoWorker : WorkerImpl<AutoWorker> {

    val instanceLock = getLock()

    val instances = mutableMapOf<UUID<AutoBackend>, AutoBackend>()

    override suspend fun run() {
        // worker is event-driven
    }

    fun register(backend: AutoBackend) {
        instanceLock.use {
            instances[backend.globalId] = backend
        }
    }

    fun unregister(backend: AutoBackend) {
        instanceLock.use {
            instances.remove(backend.globalId)
        }
    }

    fun connect(handle: AutoHandle, connector: AutoConnector, connectingPeerTime: LamportTimestamp): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle.globalId]) { "missing auto instance: $handle" }
        }.let {
            it.addPeer(connector, connectingPeerTime)
        }

    fun receive(globalId: UUID<AutoBackend>, operation: AutoOperation) {
        instanceLock.use {
            instances[globalId]?.receive(operation, false)
        }
    }

}