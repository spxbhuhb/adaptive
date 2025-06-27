package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend

@Adat
class Redo : SheetOperation() {

    override fun commit(controller: SheetViewBackend): OperationResult {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun revert(controller: SheetViewBackend) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Redo"

}