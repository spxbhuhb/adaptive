package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.originList
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.iot.model.Thermostat
import `fun`.adaptive.cookbook.iot.model.ThermostatStatus
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.fourRandomInt
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.delay
import kotlin.math.abs

class ThermostatWorker : WorkerImpl<ThermostatWorker> {

    val count = 100

    val autoWorker by worker<AutoWorker>()

    val lock = getLock()

    lateinit var thermostats: AdatClassListFrontend<Thermostat>

    override suspend fun run() {
        initList()

        while (isActive) {
            val randoms = fourRandomInt().map { abs(it) }
            val index = (randoms[0] % 10) // change the first 10, so it is visible
            val thermostat = thermostats[index]

            when (randoms[1] % 3) {
                0 -> thermostat.actual.update { thermostat.actual + 5 }
                1 -> thermostat.actual.update { thermostat.actual - 5 }
                2 -> thermostat.status.update { ThermostatStatus.entries[(thermostat.status.ordinal + 1) % 6] }
            }

            delay(50)
        }
    }

    fun initList() {
        lock.use {
            thermostats = originList(autoWorker, Thermostat)
        }

        (1 .. count).map {
            val randoms = fourRandomInt()

            thermostats +=
                Thermostat(
                    id = UUID(),
                    localId = "TH-${it.toString().padStart(3, '0')}",
                    floor = "Floor ${abs(randoms[0] % 5) + 1}",
                    name = "Thermostat - $it",
                    group = "Group (${abs(randoms[1] % 5) + 1})",
                    actual = 22.0,
                    setPoint = 21.0,
                    target = 20.5,
                    status = ThermostatStatus.entries[abs(randoms[2] % 6) + 1]
                )
        }
    }

    fun connectInfo() =
        lock.use {
            thermostats.connectInfo()
        }

}