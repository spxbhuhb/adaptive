package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend

@Adat
class Undo : SheetOperation() {

    override fun commit(controller: SheetViewBackend): OperationResult {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun revert(controller: SheetViewBackend) {
        throw UnsupportedOperationException("implemented in the engine")
    }

    override fun toString(): String = "Undo"

}