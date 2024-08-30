package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.components.quickFilter
import `fun`.adaptive.cookbook.shared.f12
import `fun`.adaptive.cookbook.shared.f16
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.format
import `fun`.adaptive.utility.fourRandomInt
import kotlin.math.abs

@Adat
class ThermostatFilter(
    val status: Status,
    val text: String
) {
    fun matches(thermostat: Thermostat): Boolean {
        println(thermostat)
        return (text.isEmpty() || thermostat.name.contains(text, ignoreCase = true)) &&
            (status == Status.All || thermostat.status == status)
    }
}

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
    val status: Status
)

enum class Status(
    val label: String
) {
    All("Összes"),
    Heating("Hűtés"),
    Cooling("Fűtés"),
    Off("Off"),
    On("On"),
    Offline("Offline"),
    SOS("SOS");
}

@Adaptive
fun thermostats() {
    val filter = copyStore { ThermostatFilter(Status.All, "") }

    println(filter)

    val thermostats = (1 .. 100).map {
        Thermostat(
            id = UUID(),
            localId = "TH-${it.toString().padStart(3, '0')}",
            floor = "${abs(fourRandomInt()[0] % 5) + 1}. em",
            name = "Thermostat - 1",
            group = "Tárgyalók",
            actual = 22.0,
            setPoint = 21.0,
            target = 20.5,
            status = Status.entries[abs(fourRandomInt()[0] % 6) + 1]
        )
    }.filter { filter.matches(it) }

    quickFilter(filter.status, Status.entries.map { it.name to it.label }) { filter.status.update { it } }

    for (thermostat in thermostats) {
        thermostat(thermostat)
    }
}

@Adaptive
fun thermostat(thermostat: Thermostat) {
    row {
        width { 600.dp } .. spaceBetween .. alignItems.center .. padding(16.dp) .. backgroundColor(color(0xffffff))
        cornerRadius(16.dp)
//        box {
//            svg()
//        }
        text(thermostat.localId) .. semiBoldFont .. f16
        text(thermostat.floor) .. f16
        text(thermostat.name) .. f16
        text(thermostat.group) .. f12
        text(thermostat.actual.format(1, hideZeroDecimals = true) + " ºC") .. f12
        text(thermostat.setPoint.format(1, hideZeroDecimals = true) + " ºC") .. f12
        text(thermostat.target.format(1, hideZeroDecimals = true) + " ºC") .. f12
        status(thermostat.status)
    }
}

private val coolingColor = color(0x29B6C7)
private val onColor = color(0x00FF08)
private val offlineColor = color(0xC72929)
private val offColor = color(0x525151)
private val sosColor = color(0xC72929)
private val heatingColor = color(0x8B7A0D)

private val statusBoxStyles = arrayOf(
    instructionsOf(border(color(0), 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(heatingColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(coolingColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(offColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(onColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(offlineColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
    instructionsOf(border(sosColor, 2.dp), cornerRadius(14.dp), size(50.dp, 21.dp)),
)

private val statusTextStyles = arrayOf(
    textColor(0),
    textColor(heatingColor),
    textColor(coolingColor),
    textColor(offColor),
    textColor(onColor),
    textColor(offlineColor),
    textColor(sosColor)
)

@Adaptive
private fun status(status: Status) {
    row {
        statusBoxStyles[status.ordinal] .. alignItems.center
        text(status) .. statusTextStyles[status.ordinal] .. fontSize(9.sp)
    }
}