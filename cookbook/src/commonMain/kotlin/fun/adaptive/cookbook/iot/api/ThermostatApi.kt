package `fun`.adaptive.cookbook.iot.api

import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ThermostatApi {

    suspend fun list(): AutoConnectInfo

    suspend fun setTemperature(temperature: Int)

}
