package `fun`.adaptive.cookbook.iot.api

import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.cookbook.iot.model.Thermostat
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ThermostatApi {

    suspend fun list(): AutoConnectionInfo<List<Thermostat>>

    suspend fun setTemperature(temperature: Int)

}
