package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewModel

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

}