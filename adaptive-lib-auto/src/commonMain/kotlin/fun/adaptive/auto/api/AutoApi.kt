package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoApi {

    suspend fun peerTime(handle: AutoHandle): LamportTimestamp

    suspend fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingTime: LamportTimestamp): LamportTimestamp
    suspend fun removePeer(handle: AutoHandle)

    suspend fun add(handle: AutoHandle, operation: AutoAdd)
    suspend fun modify(handle: AutoHandle, operation: AutoModify)
    suspend fun move(handle: AutoHandle, operation: AutoMove)
    suspend fun remove(handle: AutoHandle, operation: AutoRemove)

}