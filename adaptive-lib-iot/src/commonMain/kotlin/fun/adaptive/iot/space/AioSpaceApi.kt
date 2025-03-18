package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface AioSpaceApi {

    suspend fun query(): List<AioItem>

    suspend fun add(
        parentId: UUID<AioItem>?,
        displayOrder: Int
    ): AioSpace

    suspend fun update(space: AioItem)

}