package `fun`.adaptive.iot.api

import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface AioProjectApi {

    suspend fun create(): AioProject

}