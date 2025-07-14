package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.row

@Adaptive
fun table(
    backend: TableViewBackend
): AdaptiveFragment {

    val observed = observe { backend }

    row {
        for (cell in observed.cells) {
            tableHeaderCell(cell)
        }
    }

    return fragment()
}