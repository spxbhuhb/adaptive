package `fun`.adaptive.sandbox.recipe.document.xlsx

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.xlsx.conf.XlsxConfiguration
import `fun`.adaptive.xlsx.fillRow
import `fun`.adaptive.xlsx.model.XlsxDocument
import `fun`.adaptive.xlsx.save
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlin.random.Random

/**
 * # XLSX build and download
 *
 * - Build an XLSX file with [XlsxDocument](class://).
 * - use the [save](function://XlsxDocument) to initiate the download
 */
@Adaptive
fun xlsxDownloadExample(): AdaptiveFragment {

    submitButton("Click here do download the file") {
        buildAndInitDownload(adapter())
    }

    return fragment()
}

private fun buildAndInitDownload(
    adapter: AdaptiveAdapter
) {
    val cfg = XlsxConfiguration()

    val roundedAndThousandSeparatedNumberFormat = cfg.formats.newCustomNumberFormat("#,##0.000")

    val xlsx = XlsxDocument(cfg)

    val sheet = xlsx.newSheet("T2 Database & Task")

    sheet.columns["A"].width = 18.5
    sheet.columns["B"].width = 12.5

    sheet["A1"].value = "Name"
    sheet["B1"].value = "Date of birth"
    sheet["C1"].value = "Still alive"

    sheet.fillRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
    sheet.fillRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))

    val summary = xlsx.newSheet("Summary")

    summary.columns["A"].width = 18.2
    summary.columns["B"].width = 22.5

    summary["A1"].value = "Mission start"
    summary["B1"].value = Clock.System.now()

    summary["A2"].value = "Population to examine"
    summary["B2"].value = Random.nextDouble(9_000.0, 20_000.0)
    summary["B2"].numberFormat = roundedAndThousandSeparatedNumberFormat

    adapter.scope.launch {
        xlsx.save("test.xlsx")
    }
}