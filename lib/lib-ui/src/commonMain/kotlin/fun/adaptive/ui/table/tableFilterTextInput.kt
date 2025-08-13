package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.input.text.textInput

@Adaptive
fun tableFilterTextInput(
    tableBackend : TableViewBackend<*>
) {
    textInput(tableBackend.filterTextBackend) .. tableBackend.tableTheme.filterText
}