package `fun`.adaptive.ui.support.statistics

import `fun`.adaptive.adat.Adat

@Adat
class AuiStatistics(
    var scheduleUpdate : Int = 0,
    var computeLayout : Int = 0,
    var placeLayout : Int = 0,
    var updateLayout : Int = 0,
    var shouldUpdateSelfTrue : Int = 0,
    var shouldUpdateSelfFalse : Int = 0
) {
    fun dump() : String {
        return "$scheduleUpdate, $computeLayout, $placeLayout, $updateLayout, $shouldUpdateSelfTrue, $shouldUpdateSelfFalse"
    }
}