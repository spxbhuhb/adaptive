package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.boxWithProposal
import `fun`.adaptive.ui.api.cellBox
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fillStrategy
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.layout.cellbox.CellBoxArrangement
import `fun`.adaptive.ui.fragment.layout.cellbox.CellBoxArrangementCalculator
import `fun`.adaptive.ui.fragment.layout.cellbox.CellDef
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun <ITEM> table(
    backend: TableViewBackend<ITEM>
): AdaptiveFragment {

    val observed = observe { backend }

    boxWithProposal { proposal ->
        tableInner(observed, proposal)
    }

    return fragment()
}

@Adaptive
fun <ITEM> tableInner(
    backend: TableViewBackend<ITEM>,
    proposal: SizingProposal
) {
    val arrangement = CellBoxArrangementCalculator(adapter() as AbstractAuiAdapter<*, *>)
        .findBestArrangement(
            cells = backend.cells.map { CellDef(null, it.minWidth, it.width) },
            proposal.maxWidth - backend.tableTheme.arrangementWidthAdjustment,
            backend.gap.width!!.value
        )

    column {
        width { proposal.maxWidth.dp } .. height { proposal.maxHeight.dp } .. fillStrategy.constrain

        cellBox(arrangement = arrangement) {
            width { proposal.maxWidth.dp }

            backend.tableTheme.headerContainer
            for (cell in backend.cells) {
                @Suppress("UNCHECKED_CAST")
                tableHeaderCell(cell as TableCellDef<ITEM, Any>)
            }
        }

        column {
            backend.tableTheme.contentContainer .. width { proposal.maxWidth.dp }

            for (item in backend.viewportItems ?: emptyList()) {
                tableItem(backend, arrangement, item)
            }
        }
    }
}

@Adaptive
fun <ITEM> tableItem(
    backend: TableViewBackend<ITEM>,
    arrangement : CellBoxArrangement,
    item: TableItem<ITEM>
): AdaptiveFragment {

    cellBox(arrangement = arrangement) {
        backend.tableTheme.itemContainer

        for (cell in backend.cells) {
            @Suppress("UNCHECKED_CAST")
            tableCell(cell as TableCellDef<ITEM,Any>, item, cell.contentFun)
        }
    }

    return fragment()
}

@Adaptive
fun <ITEM, CELL_DATA> tableCell(
    cellDef: TableCellDef<ITEM, CELL_DATA>,
    item: TableItem<ITEM>,
    content: @Adaptive (TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any
): AdaptiveFragment {
    content(cellDef, item.data)
    return fragment()
}