package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewController

class Redo : SheetOperation() {

    override fun commit(controller: SheetViewController): OperationResult {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun revert(controller: SheetViewController) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Redo"

}