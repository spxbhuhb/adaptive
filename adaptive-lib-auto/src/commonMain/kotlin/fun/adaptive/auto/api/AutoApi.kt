package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.model.operation.*
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AutoApi {

    suspend fun peerTime(handle: AutoHandle): LamportTimestamp

    suspend fun addPeer(origin: AutoHandle, connecting: AutoHandle, connectingTime: LamportTimestamp): LamportTimestamp

    /**
     * Remove the client specified by [handle] from the backend specified by [AutoHandle.globalId].
     */
    suspend fun removePeer(handle: AutoHandle)

    suspend fun add(handle: AutoHandle, operation: AutoAdd)
    suspend fun modify(handle: AutoHandle, operation: AutoUpdate)
    suspend fun move(handle: AutoHandle, operation: AutoMove)
    suspend fun remove(handle: AutoHandle, operation: AutoRemove)
    suspend fun empty(handle: AutoHandle, operation: AutoEmpty)
    suspend fun syncBatch(handle: AutoHandle, operation: AutoSyncBatch)
    suspend fun syncEnd(handle: AutoHandle, operation: AutoSyncEnd)

}