package `fun`.adaptive.cookbook.iot.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID

@Adat
class Thermostat(
    val id: UUID<Thermostat>,
    val localId: String,
    val floor: String,
    val name: String,
    val group: String,
    val actual: Double,
    val setPoint: Double,
    val target: Double,
    val status: ThermostatStatus
)
