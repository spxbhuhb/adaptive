package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.iot.api.ThermostatApi
import `fun`.adaptive.cookbook.iot.model.Thermostat

class ThermostatService : ThermostatApi, ServiceImpl<ThermostatService> {

    val thermostatWorker by worker<ThermostatWorker>()

    override suspend fun list(): AutoConnectionInfo<List<Thermostat>> {
        publicAccess()
        return thermostatWorker.connectInfo()
    }

    override suspend fun setTemperature(temperature: Int) {
        ensureLoggedIn()
        TODO("Not yet implemented")
    }

}