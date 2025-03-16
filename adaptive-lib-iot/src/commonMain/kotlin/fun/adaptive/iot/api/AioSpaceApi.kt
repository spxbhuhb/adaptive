package `fun`.adaptive.iot.api

import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface AioSpaceApi {

    suspend fun query() : List<AioSpace>

    suspend fun add(projectId: UUID<AioProject>, parentId: UUID<AioSpace>?, type: AioSpaceType): AioSpace

    suspend fun update(space: AioSpace)

}