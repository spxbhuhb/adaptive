package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.utility.UUID

abstract class AutoConnector {

    abstract suspend fun add(globalId: UUID<AutoBackend>, operation: AutoAdd)

    abstract suspend fun modify(globalId: UUID<AutoBackend>, operation: AutoModify)

    abstract suspend fun move(globalId: UUID<AutoBackend>, operation: AutoMove)

    abstract suspend fun remove(globalId: UUID<AutoBackend>, operation: AutoRemove)

    abstract suspend fun transaction(globalId: UUID<AutoBackend>, operation: AutoTransaction)

}