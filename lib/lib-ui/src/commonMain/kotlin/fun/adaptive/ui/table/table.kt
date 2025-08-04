package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentTraceContext
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.boxWithProposal
import `fun`.adaptive.ui.api.cellBox
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.layout.cellbox.CellBoxArrangement
import `fun`.adaptive.ui.fragment.layout.cellbox.CellBoxArrangementCalculator
import `fun`.adaptive.ui.fragment.layout.cellbox.CellDef
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds

@Adaptive
fun <ITEM> table(
    backend: TableViewBackend<ITEM>
): AdaptiveFragment {

    val observed = observe { backend }

    boxWithProposal { proposal ->
        tableContent(observed, proposal)
    }

    return fragment()
}

@Adaptive
fun <ITEM> tableContent(
    backend: TableViewBackend<ITEM>,
    proposal: SizingProposal
) {
    val arrangement = CellBoxArrangementCalculator(adapter() as AbstractAuiAdapter<*, *>)
        .findBestArrangement(
            cells = backend.cells.map { CellDef(null, it.minWidth, it.width) },
            proposal.maxWidth,
            backend.gap.width!!.value
        )

    column {
        width { proposal.maxWidth.dp } .. height { proposal.maxHeight.dp } .. verticalScroll

        for (item in backend.viewportItems ?: emptyList()) {
            tableItem(backend, arrangement, item)
        }
    }
}

@Adaptive
fun <ITEM> tableItem(
    backend: TableViewBackend<ITEM>,
    arrangement : CellBoxArrangement,
    item: TableItem<ITEM>
): AdaptiveFragment {

    println(arrangement)
    println(backend.cells)

    cellBox(arrangement = arrangement) {
        for (cell in backend.cells) {
            tableCell(cell, item)
        }
    } .. backgrounds.friendlyOpaque .. gap { 16.dp }

    return fragment()
}

@Adaptive
fun <ITEM, CELL_DATA> tableCell(
    cellDef: TableCellDef<ITEM, CELL_DATA>,
    item: TableItem<ITEM>
): AdaptiveFragment {
    adapter().traceWithContext = true
    localContext(FragmentTraceContext()) {
        //cellDef.contentFun(cellDef, item.data)
    }
    return fragment()
}