package `fun`.adaptive.iot.driver.api

import `fun`.adaptive.iot.driver.request.AioDriverRequest
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioDriverApi {

    suspend fun process(request: AioDriverRequest)

}