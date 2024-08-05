package hu.simplexion.adaptive.auto.service

import hu.simplexion.adaptive.auto.FragmentStore
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.api.AutoApi
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.connector.ServiceConnector
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.auto.worker.AutoWorker
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.utility.UUID

class AutoService : ServiceImpl<AutoService>, AutoApi {

    val worker by worker<AutoWorker>()

    override suspend fun connect(handle: AutoHandle, connectingPeerTime: LamportTimestamp): LamportTimestamp =
        worker.connect(
            handle,
            ServiceConnector(serviceContext),
            connectingPeerTime
        )

    override suspend fun add(globalId: UUID<AutoBackend>, operation: AutoAdd) {
        worker.receive(globalId, operation)
    }

    override suspend fun modify(globalId: UUID<AutoBackend>, operation: AutoModify) {
        worker.receive(globalId, operation)
    }

    override suspend fun move(globalId: UUID<AutoBackend>, operation: AutoMove) {
        worker.receive(globalId, operation)
    }

    override suspend fun remove(globalId: UUID<AutoBackend>, operation: AutoRemove) {
        worker.receive(globalId, operation)
    }

    override suspend fun transaction(globalId: UUID<AutoBackend>, operation: AutoTransaction) {
        worker.receive(globalId, operation)
    }

    override suspend fun disconnect(globalId: UUID<FragmentStore>) {
        TODO("Not yet implemented")
    }
}