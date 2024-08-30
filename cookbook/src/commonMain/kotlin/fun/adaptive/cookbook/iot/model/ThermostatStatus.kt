package `fun`.adaptive.cookbook.iot.model

enum class ThermostatStatus(
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