package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.cookbook.iot.model.Thermostat
import `fun`.adaptive.cookbook.iot.model.ThermostatStatus
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

fun iotCommon() {
    WireFormatRegistry += Thermostat
    WireFormatRegistry["fun.adaptive.cookbook.iot.model.ThermostatStatus"] = EnumWireFormat(ThermostatStatus.entries)
}