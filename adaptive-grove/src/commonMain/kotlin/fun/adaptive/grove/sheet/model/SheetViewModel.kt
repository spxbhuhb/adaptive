package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.SheetEngine
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.grove.sheet.operation.SheetOperation
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.utility.UUID

class SheetViewModel(
    val engine: SheetEngine,
    fragments: Collection<LfmDescendant>
) {
    val emptySelection = SheetSelection(emptyList())

    val fragments = autoCollectionOrigin(fragments)
    val selection = autoItemOrigin(emptySelection)
    var clipboard = SheetClipboard(emptyList())

    lateinit var root: GroveDrawingLayer

    operator fun plusAssign(operation: SheetOperation) {
        engine.execute(operation)
//        val result = engine.operations.trySend(operation)
//        if (result.isFailure) {
//            engine.logger.error("cannot send operation: $operation, reason: ${result.exceptionOrNull()}")
//        }
    }

    operator fun plusAssign(fragment: LfmDescendant) {
        fragments += fragment
        root += fragment
    }

    operator fun minusAssign(fragment: LfmDescendant) {
        fragments.remove { it.uuid == fragment.uuid }
        root -= fragment
    }

}