package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.CleanupHandler

class AutoService : ServiceImpl<AutoService>, AutoApi {

    val worker by worker<AutoWorker>()

    class PeerCleanup(
        worker: AutoWorker,
        handle: AutoHandle
    ) : CleanupHandler<ServiceSession>(
        {
            worker.removePeer(handle)
        }
    )

    override suspend fun peerTime(handle: AutoHandle): LamportTimestamp =
        worker.peerTime(handle)

    override suspend fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingTime: LamportTimestamp): LamportTimestamp {

        val originTime = worker.addPeer(
            origin,
            connecting,
            requireNotNull(serviceContext.transport) { "no transport in the service context" }
        )

        // FIXME shouldn't this be context cleanup
        serviceContext.addSessionCleanup(PeerCleanup(worker, connecting))

        return originTime
    }

    override suspend fun removePeer(handle: AutoHandle) {
        worker.removePeer(handle)
    }

    override suspend fun add(handle: AutoHandle, operation: AutoAdd) {
        worker.receive(handle, operation)
    }

    override suspend fun update(handle: AutoHandle, operation: AutoUpdate) {
        worker.receive(handle, operation)
    }

    override suspend fun move(handle: AutoHandle, operation: AutoMove) {
        worker.receive(handle, operation)
    }

    override suspend fun remove(handle: AutoHandle, operation: AutoRemove) {
        worker.receive(handle, operation)
    }

    override suspend fun empty(handle: AutoHandle, operation: AutoEmpty) {
        worker.receive(handle, operation)
    }

    override suspend fun syncBatch(handle: AutoHandle, operation: AutoSyncBatch) {
        worker.receive(handle, operation)
    }

    override suspend fun syncEnd(handle: AutoHandle, operation: AutoSyncEnd) {
        worker.receive(handle, operation)
    }
}