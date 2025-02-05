package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.value.adaptiveValueStore
import `fun`.adaptive.grove.sheet.SheetEngine
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.grove.sheet.operation.SheetOperation
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.render.model.AuiRenderData
import kotlin.collections.plusAssign
import kotlin.math.max
import kotlin.math.min

class SheetViewModel(
    val engine: SheetEngine,
) {

    val emptySelection = SheetSelection(emptyList())

    /**
     * This list contains all sheet items in the whole lifetime of the sheet,
     * even deleted items.
     *
     * The reason for this is the cost of access which must be kept as minimum
     * as multi-item operations can become very costly if we have to perform
     * lookups and list reshuffling.
     *
     * So, lookups use the `index` of the item which is the index in this
     * list. When an item is deleted, it is marked by setting the `removed`
     * flag in [SheetItem].
     */
    val items = mutableListOf<SheetItem>()

    val nextIndex
        get() = items.size

    var selection = emptySelection
        set(value) {
            field = value
            selectionRevision.value = selectionRevision.value + 1
        }

    var clipboard = SheetClipboard(emptyList())

    val modelRevision = adaptiveValueStore { 0 }
    val selectionRevision = adaptiveValueStore { 0 }

    lateinit var root: GroveDrawingLayer


    operator fun plusAssign(operation: SheetOperation) {
        engine.execute(operation)
//        val result = engine.operations.trySend(operation)
//        if (result.isFailure) {
//            engine.logger.error("cannot send operation: $operation, reason: ${result.exceptionOrNull()}")
//        }
    }

    operator fun plusAssign(item: SheetItem) {
        if (item.removed == true) {
            item.removed = false
        } else {
            items += item
        }
        root += item
        modelRevision.value = modelRevision.value + 1
    }

    operator fun minusAssign(index : Int) {
        val item = items[index]
        if (item.removed) return

        item.removed = true
        root -= item
        modelRevision.value = modelRevision.value + 1
    }

    fun select() {
        selection = emptySelection
    }

    fun select(startPosition: RawPosition, x: Double, y: Double, add: Boolean) {
        val x1 = min(startPosition.left, x)
        val y1 = min(startPosition.top, y)
        val x2 = max(startPosition.left, x)
        val y2 = max(startPosition.top, y)

        select(add) { renderData ->
            val rx1 = renderData.finalLeft
            val ry1 = renderData.finalTop
            val rx2 = rx1 + renderData.finalWidth
            val ry2 = ry1 + renderData.finalHeight

            when {
                x1 >= rx2 || rx1 >= x2 -> false          // one rectangle is to the left of the other
                y1 >= ry2 || ry1 >= y2 -> false          // one rectangle is above the other
                else -> true
            }
        }
    }

    fun select(x: Double, y: Double, add: Boolean) {
        select(add) { it.contains(x, y) }
    }

    fun select(add: Boolean, condition: (renderData: AuiRenderData) -> Boolean) {

        val box = root.parent?.parent as AbstractContainer<*, *>

        val selectedItems = mutableListOf<SheetItem>()

        for (child in box.layoutItems) {

            if (! condition(child.renderData)) continue

            val info = child.itemInfo(box) ?: continue

            selectedItems += items[info.index]
        }

        // if the selection is already up to date we don't have to refresh it
        // this prevents unnecessary undo/redo operations if the user clicks
        // on the same item a few times

        selectedItems.sortBy { it.index }
        if (selectedItems == selection.items) return

        if (add) {
            val indices = selectedItems.map { it.index }
            selection.items.forEach {
                if (it.index in indices) return@forEach
                items += it
            }
        }

        this += Select(items)
    }

    fun SheetViewModel.refreshSelection() {
        val box = root.parent?.parent as AbstractContainer<*, *>
        val selected = selection.value.selected.map { it.uuid }
        val items = mutableListOf<SelectionInfo>()

        for (child in box.layoutItems) {
            val info = child.itemInfo(box) ?: continue
            if (info.uuid !in selected) continue
            items += SelectionInfo(info.uuid, child.renderData.rawFrame())
        }

        selection.update(SheetSelection(items))
    }

    private fun AuiRenderData.contains(x: Double, y: Double) =
        when {
            y < finalTop -> false
            x < finalLeft -> false
            x >= finalLeft + finalWidth -> false
            y >= finalTop + finalHeight -> false
            else -> true
        }

    private fun AuiRenderData.rawFrame() =
        RawFrame(finalTop, finalLeft, finalWidth, finalHeight)

    private fun AdaptiveFragment?.itemInfo(sheet: AbstractContainer<*, *>): ItemInfo? {
        var current = this

        while (current != null) {
            val info = current.instructions.firstInstanceOfOrNull<ItemInfo>()
            if (info != null) return info

            current = current.parent
            if (current == sheet) return null
        }

        return null
    }

    inline fun forSelection(action: (item: SheetItem) -> Unit) {
        selection.items.forEach { item ->
            action(item)
        }
    }

}