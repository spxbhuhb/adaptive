package `fun`.adaptive.auto.service

import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.connector.ServiceConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.service.getService

class AutoService : ServiceImpl<AutoService>, AutoApi {

    val worker by worker<AutoWorker>()

    override suspend fun peerTime(handle: AutoHandle): LamportTimestamp =
        worker.peerTime(handle)

    override suspend fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingTime: LamportTimestamp): LamportTimestamp =
        worker.addPeer(
            origin,
            ServiceConnector(connecting, getService(serviceContext.transport), worker.scope, 1000),
            connectingTime
        )

    override suspend fun add(handle: AutoHandle, operation: AutoAdd) {
        worker.receive(handle, operation)
    }

    override suspend fun modify(handle: AutoHandle, operation: AutoModify) {
        worker.receive(handle, operation)
    }

    override suspend fun move(handle: AutoHandle, operation: AutoMove) {
        worker.receive(handle, operation)
    }

    override suspend fun remove(handle: AutoHandle, operation: AutoRemove) {
        worker.receive(handle, operation)
    }

    override suspend fun removePeer(handle: AutoHandle) {
        TODO("Not yet implemented")
    }
}