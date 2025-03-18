package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.project.model.AioProject
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface AioSpaceApi {

    suspend fun query() : List<AioSpace>

    suspend fun add(
        projectId: UUID<AioProject>,
        parentId: UUID<AioSpace>?,
        type: AioSpaceType,
        displayOrder: Int
    ): AioSpace

    suspend fun update(space: AioSpace)

}