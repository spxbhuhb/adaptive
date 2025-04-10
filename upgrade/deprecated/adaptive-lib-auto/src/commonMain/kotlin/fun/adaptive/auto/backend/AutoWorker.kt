package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.internal.connector.ServiceConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.AutoOperation
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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

    fun register(instance: AutoGeneric) {
        instanceLock.use {
            instances[instance.globalId()] = instance
        }
    }

    fun deregister(instance: AutoGeneric) {
        instanceLock.use {
            instances.remove(instance.globalId())
        }
    }

    fun peerTime(handle: AutoHandle): LamportTimestamp =
        instanceLock.use {
            checkNotNull(instances[handle.globalId]) { "missing auto instance: $handle" }
        }
            .time

    fun addPeer(
        thisHandle: AutoHandle,
        connectingHandle: AutoHandle,
        transport: ServiceCallTransport
    ): LamportTimestamp {
        val instance = instanceLock.use {
            checkNotNull(instances[thisHandle.globalId]) { "missing auto instance: $thisHandle" }
        }

        val connector = ServiceConnector(
            instance,
            getService(transport),
            connectingHandle,
            initiator = false
        )

        val time = instance.addConnector(connector)

        scope.launch { supervisorScope {  connector.run() } }

        return time
    }

    fun removePeer(handle: AutoHandle) =
        instanceLock.use {
            // Decided not to throw an exception here as this is a pretty normal occurrence after
            // server start when the connected peers try to disconnect from with the old handle.
            // TODO should we try and persist handles when it's possible?
            instances[handle.globalId]?.removeConnector(handle)
        }

    fun receive(handle: AutoHandle, operation: AutoOperation) {
        instanceLock.use {
            instances[handle.globalId]?.remoteReceive(operation)
        }
    }

}