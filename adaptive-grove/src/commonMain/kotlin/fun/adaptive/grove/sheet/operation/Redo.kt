package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewModel

class Redo : SheetOperation() {

    override fun applyTo(viewModel: SheetViewModel) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Redo"

}