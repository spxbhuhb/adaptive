package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.SheetClipboard
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.fragment.layout.RawFrame

abstract class SheetOperation {

    var firstRun: Boolean = true

    /**
     * Commit the operation to the sheet:
     *
     * - save whatever information is needed for [revert]
     * - change the fragments of the sheet as needed
     *
     * @return  true if this operation should replace the last one on stack, false otherwise
     */
    abstract fun commit(viewModel: SheetViewModel): Boolean

    /**
     * Revert a previous commit.
     */
    abstract fun revert(viewModel: SheetViewModel)

    fun SheetViewModel.selectionToClipboard(): SheetClipboard {
        val modelData = mutableListOf<LfmDescendant>()
        val frameData = mutableListOf<RawFrame>()

        forSelection {
            modelData += it.model
            frameData += it.frame
        }

        return SheetClipboard(modelData, frameData)
    }
}