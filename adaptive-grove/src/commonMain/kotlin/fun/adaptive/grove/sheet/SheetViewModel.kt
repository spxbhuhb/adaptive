package `fun`.adaptive.grove.sheet

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.fragment.GroveSheetInner
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

    lateinit var root : GroveSheetInner

    operator fun plusAssign(operation: SheetOperation) {
        val result = engine.operations.trySend(operation)
        if (result.isFailure) {
            engine.logger.error("cannot send operation: $operation, reason: ${result.exceptionOrNull()}")
        }
    }

    fun select(x: Double, y: Double) {

        val box = root.parent?.parent as AbstractContainer<*, *>

        val items = mutableListOf<SelectionInfo>()

        for (child in box.layoutItems) {

            if (! child.renderData.contains(x, y)) continue

            val info = child.descendantInfo(box) ?: continue

            items += SelectionInfo(info.uuid, child.renderData.rawFrame())
        }

        // if the selection is already up to date we don't have to refresh it
        // this prevents unnecessary undo/redo operations if the user clicks
        // on the same item a few times

        if (selection.value == SheetSelection(items)) return

        this += Select(items)
    }

    fun refreshSelection() {
        val box = root.parent?.parent as AbstractContainer<*, *>
        val selected = selection.value.selected.map { it.uuid }
        val items = mutableListOf<SelectionInfo>()

        for (child in box.layoutItems) {
            val info = child.descendantInfo(box) ?: continue
            if (info.uuid !in selected) continue
            items += SelectionInfo(info.uuid, child.renderData.rawFrame())
        }

        selection.update(SheetSelection(items))
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
            val info = current.instructions.firstInstanceOfOrNull<DescendantInfo>()
            if (info != null) return info

            current = current.parent
            if (current == sheet) return null
        }

        return null
    }

    fun forEachSelected(block : (model : LfmDescendant, fragment : AdaptiveFragment) -> Unit) {
        selection.value.selected.forEach {
            val info = DescendantInfo(it.uuid)
            val model = fragments.first { it.uuid == it.uuid }
            val fragment = root.children.first { info in it.instructions }
            block(model, fragment)
        }
    }

    companion object {
        val emptySelection = SheetSelection(emptyList())
    }

}