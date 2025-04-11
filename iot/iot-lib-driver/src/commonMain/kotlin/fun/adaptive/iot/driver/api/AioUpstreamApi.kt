package `fun`.adaptive.iot.driver.api

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId

@ServiceApi
interface AioUpstreamApi {

    suspend fun connect(networkId: AvValueId)

}