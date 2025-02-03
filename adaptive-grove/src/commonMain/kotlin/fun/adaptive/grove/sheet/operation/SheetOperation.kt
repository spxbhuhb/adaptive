package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.DescendantInfo
import `fun`.adaptive.grove.sheet.model.SheetViewModel

abstract class SheetOperation {

    /**
     * Commit the operation to the sheet:
     *
     * - save whatever information is needed for [revert]
     * - change the fragments of the sheet as needed
     *
     * @return  true if this operation should replace the last one on stack, false otherwise
     */
    abstract fun commit(viewModel: SheetViewModel) : Boolean

    /**
     * Revert a previous commit.
     */
    abstract fun revert(viewModel: SheetViewModel)

    fun SheetViewModel.forEachSelected(block : (model : LfmDescendant, fragment : AdaptiveFragment) -> Unit) {
        selection.value.selected.forEach { info ->
            val info = DescendantInfo(info.uuid)
            val model = fragments.first { info.uuid == it.uuid }
            val fragment = root.children.first { info in it.instructions }
            block(model, fragment)
        }
    }
}