package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position

class Paste : SheetOperation() {

    val undoData = mutableListOf<Add>()

    override fun commit(viewModel: SheetViewModel): Boolean {

        val descendants = viewModel.clipboard.descendants

        descendants.forEach { descendant ->
            val position = descendant.instructions.lastInstanceOfOrNull<Position>() ?: Position.ZERO
            val add = Add(position.left + 10.dp, position.top + 10.dp, descendant)
            add.commit(viewModel)
            undoData += add
        }

        // This is tricky as the layout is not run yet, so technically we
        // do not know the final positions and sizes. We have this information
        // from `frame`, but there is actually no guarantee that the fragment
        // will have the same size as before.

        // I'll go with the saved frame for now and revise this later.

        // TODO revise paste selection frame

        viewModel.selection.update(SheetSelection(emptyList()))

        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        undoData.forEach { it.revert(viewModel) }
        undoData.clear()
    }

    override fun toString(): String =
        "Paste"
}