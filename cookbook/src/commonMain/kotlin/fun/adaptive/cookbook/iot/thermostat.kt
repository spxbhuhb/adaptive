package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.cookbook.iot.api.ThermostatApi
import `fun`.adaptive.cookbook.iot.model.Thermostat
import `fun`.adaptive.cookbook.iot.model.ThermostatStatus
import `fun`.adaptive.cookbook.shared.f12
import `fun`.adaptive.cookbook.shared.f16
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.service.getService
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.utility.format

@Adat
data class ThermostatFilter(
    val status: ThermostatStatus,
    val text: String
) {
    fun matches(thermostat: Thermostat): Boolean {
        return (text.isEmpty() || thermostat.name.contains(text, ignoreCase = true)) &&
            (status == ThermostatStatus.All || thermostat.status == status)
    }
}

@Adaptive
fun thermostats() {
    val filter = copyStore { ThermostatFilter(ThermostatStatus.All, "") }

    val all = autoList(Thermostat) { getService<ThermostatApi>(adapter().transport).list() }
    val filtered = all?.filter { filter.matches(it) }

    grid {
        maxSize .. rowTemplate(38.dp, 1.fr) .. gap(16.dp)

        quickFilter(filter.status, ThermostatStatus.entries, { label }) { filter.update(filter.copy(status = it)) }

        if (filtered != null) {
            column {
                maxSize .. verticalScroll .. gap(8.dp)

                for (thermostat in filtered) {
                    thermostat(thermostat)
                }
            }
        } else {
            text("... loading ...")
        }
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
private fun status(status: ThermostatStatus) {
    row {
        statusBoxStyles[status.ordinal] .. alignItems.center
        text(status.label) .. statusTextStyles[status.ordinal] .. fontSize(9.sp)
    }
}