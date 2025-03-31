package `fun`.adaptive.iot.history.ui.model

import `fun`.adaptive.adat.Adat
import kotlin.time.Duration

@Adat
class NamedDuration(
    val type : NamedDurationType,
    val name : String,
    val duration : Duration?
) {

}

// private fun onIntervalChange(reportInterval: ReportInterval) {
//        when (reportInterval) {
//            ReportInterval.CurrentMonth -> {
//                startDateField.readOnly = true
//                endDateField.readOnly = true
//                startDateField.value = now.firstDayOfMonth
//                endDateField.value = now
//            }
//
//            ReportInterval.PreviousMonth -> {
//                startDateField.readOnly = true
//                endDateField.readOnly = true
//                startDateField.value = now.previousMonth.firstDayOfMonth
//                endDateField.value = now.previousMonth.lastDayOfMonth
//            }
//
//            ReportInterval.TwoMonthsBefore -> {
//                startDateField.readOnly = true
//                endDateField.readOnly = true
//                startDateField.value = now.twoMonthsBefore.firstDayOfMonth
//                endDateField.value = now.twoMonthsBefore.lastDayOfMonth
//            }
//
//            ReportInterval.Custom -> {
//                startDateField.readOnly = false
//                endDateField.readOnly = false
//            }
//        }
//    }