package `fun`.adaptive.grove.sheet

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.grove.sheet.operation.SheetOperation
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.render.model.AuiRenderData

open class SheetViewModel(
    val engine: SheetEngine,
    fragments: Collection<LfmDescendant>,
    selection: SheetSelection
) {

    val fragments = autoCollectionOrigin(fragments)
    val selection = autoItemOrigin(selection)

    operator fun plusAssign(operation: SheetOperation) {
        val result = engine.operations.trySend(operation)
        if (result.isFailure) {
            engine.logger.error("cannot sent operation: $operation, reason: ${result.exceptionOrNull()}")
        }
    }

    fun select(sheet: AdaptiveFragment, x: Double, y: Double) {

        check(sheet is AbstractContainer<*, *>)

        val items = mutableListOf<SelectionInfo>()

        for (child in sheet.layoutItems) {

            if (! child.renderData.contains(x, y)) continue

            val info = child.descendantInfo(sheet) ?: continue

            items += SelectionInfo(info.uuid, child.renderData.rawFrame())
        }

        this += Select(items)
    }

    fun AuiRenderData.contains(x: Double, y: Double) =
        when {
            y < finalTop -> false
            x < finalLeft -> false
            x >= finalLeft + finalWidth -> false
            y >= finalTop + finalHeight -> false
            else -> true
        }

    fun AuiRenderData.rawFrame() =
        RawFrame(finalTop, finalLeft, finalWidth, finalHeight)

    fun AdaptiveFragment?.descendantInfo(sheet: AbstractContainer<*, *>): DescendantInfo? {
        var current = this

        while (current != null) {
            val info = current.instructions.firstOrNullIfInstance<DescendantInfo>()
            if (info != null) return info

            current = current.parent
            if (current == sheet) return null
        }

        return null
    }

    companion object {
        val emptySelection = SheetSelection(emptyList())
    }

}