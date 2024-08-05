package hu.simplexion.adaptive.auto.api

import hu.simplexion.adaptive.auto.FragmentStore
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface AutoApi {

    suspend fun connect(handle: AutoHandle, connectingPeerTime: LamportTimestamp): LamportTimestamp

    suspend fun add(globalId: UUID<AutoBackend>, operation: AutoAdd)
    suspend fun modify(globalId: UUID<AutoBackend>, operation: AutoModify)
    suspend fun move(globalId: UUID<AutoBackend>, operation: AutoMove)
    suspend fun remove(globalId: UUID<AutoBackend>, operation: AutoRemove)

    suspend fun transaction(globalId: UUID<AutoBackend>, operation: AutoTransaction)

    suspend fun disconnect(globalId: UUID<FragmentStore>)

}