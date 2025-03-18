package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.iot.infrastructure.model.AioInfrastructureItem
import `fun`.adaptive.iot.project.model.AioProject
import `fun`.adaptive.iot.space.model.AioSpace
import `fun`.adaptive.iot.space.model.AioSpaceType
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID

@ServiceApi
interface AioInfrastructureApi {

    suspend fun query() : List<AioInfrastructureItem>

}