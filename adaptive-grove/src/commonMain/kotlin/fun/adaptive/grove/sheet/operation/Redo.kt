package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.model.SheetViewModel

class Redo : SheetOperation() {

    override fun commit(viewModel: SheetViewModel) : Boolean {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun revert(viewModel: SheetViewModel) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Redo"

}