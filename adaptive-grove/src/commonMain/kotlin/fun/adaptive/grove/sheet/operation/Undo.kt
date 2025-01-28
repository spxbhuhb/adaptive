package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewModel

class Undo : SheetOperation() {

    override fun applyTo(viewModel: SheetViewModel) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Undo"

}