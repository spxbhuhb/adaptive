package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioInfrastructureApi {

    suspend fun query() : List<AioInfrastructureItem>

}