package hu.simplexion.adaptive.auto.api

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.auto.model.operation.*
import hu.simplexion.adaptive.service.ServiceApi

@ServiceApi
interface AutoApi {

    suspend fun peerTime(handle: AutoHandle): LamportTimestamp

    suspend fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingTime: LamportTimestamp): LamportTimestamp
    suspend fun removePeer(handle: AutoHandle)

    suspend fun syncEnd(handle: AutoHandle, operation: AutoSyncEnd)

    suspend fun add(handle: AutoHandle, operation: AutoAdd)
    suspend fun modify(handle: AutoHandle, operation: AutoModify)
    suspend fun move(handle: AutoHandle, operation: AutoMove)
    suspend fun remove(handle: AutoHandle, operation: AutoRemove)

    suspend fun transaction(handle: AutoHandle, operation: AutoTransaction)

}