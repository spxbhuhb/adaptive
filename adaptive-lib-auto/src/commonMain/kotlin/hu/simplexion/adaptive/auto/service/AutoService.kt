package hu.simplexion.adaptive.auto.service

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.api.AutoApi
import hu.simplexion.adaptive.auto.connector.ServiceConnector
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.auto.worker.AutoWorker
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.service.getService

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