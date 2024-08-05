package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.api.AutoApi
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.utility.UUID

class ServiceConnector(
    context: ServiceContext
) : AutoConnector() {

    val service = getService<AutoApi>(context)

    override suspend fun add(globalId: UUID<AutoBackend>, operation: AutoAdd) {
        service.add(globalId, operation)
    }

    override suspend fun modify(globalId: UUID<AutoBackend>, operation: AutoModify) {
        service.modify(globalId, operation)
    }

    override suspend fun move(globalId: UUID<AutoBackend>, operation: AutoMove) {
        service.move(globalId, operation)
    }

    override suspend fun remove(globalId: UUID<AutoBackend>, operation: AutoRemove) {
        service.remove(globalId, operation)
    }

    override suspend fun transaction(globalId: UUID<AutoBackend>, operation: AutoTransaction) {
        service.transaction(globalId, operation)
    }

}