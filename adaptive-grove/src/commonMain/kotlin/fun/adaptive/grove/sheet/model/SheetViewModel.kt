package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.foundation.value.adaptiveValueStore
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.SheetEngine
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.grove.sheet.operation.SheetOperation
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.UUID
import kotlin.math.max
import kotlin.math.min

class SheetViewModel(
    val engine: SheetEngine
) {

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
        private set(value) {
            field = value
            selectionStore.value = value
        }

    val selectionStore = adaptiveValueStore { selection }

    var clipboard = SheetClipboard(emptyList(), emptyList())

    /**
     * When not zero, the next action initiated with keyboard will be run this many
     * times. For example typing 10 Ctrl-V will paste the clipboard 10 times.
     */
    val multiplier = adaptiveValueStore { 0 }

    lateinit var drawingLayer: GroveDrawingLayer<*, *>

    operator fun plusAssign(operation: SheetOperation) {
        engine.execute(operation)
//        val result = engine.operations.trySend(operation)
//        if (result.isFailure) {
//            engine.logger.error("cannot send operation: $operation, reason: ${result.exceptionOrNull()}")
//        }
    }

    fun addItem(index: Int, x: DPixel, y: DPixel, template: LfmDescendant): SheetItem {

        val templateInstructions = template.instructions

        val instanceInstructions = instructionsOf(
            templateInstructions.removeAll { it is Position || it is ItemInfo },
            ItemInfo(index),
            Position(y, x)
        )

        val instanceInstructionMapping =
            LfmMapping(
                dependencyMask = 0,
                mapping = LfmConst(
                    typeSignature<AdaptiveInstructionGroup>(),
                    instanceInstructions
                )
            )

        val instanceMapping = listOf(instanceInstructionMapping) + template.mapping.drop(1)
        val item = SheetItem(index, LfmDescendant(UUID(), template.key, instanceMapping))

        items += item
        drawingLayer += item

        return item
    }

    fun hideItem(index: Int) {
        val item = items[index]
        if (item.removed) return

        item.removed = true
        drawingLayer -= item
    }

    fun showItem(index: Int) {
        val item = items[index]
        if (! item.removed) return

        item.removed = false
        drawingLayer += item
    }

    fun select() {
        selection = emptySelection
    }

    /**
     * Direct version of select which is not put into the undo stack.
     */
    fun select(items: List<SheetItem>, containingFrame: RawFrame = SheetSelection.containingFrame(items)) {
        if (engine.trace) engine.logger.info("selecting ${items.size} items")
        selection = SheetSelection(items, containingFrame)
    }

    fun select(item: SheetItem, add: Boolean) {
        addSelectOp(mutableListOf(item), add)
    }

    /**
     * User inducted select, creates and executes an undoable select operation.
     */
    fun select(startPosition: RawPosition, x: Double, y: Double, add: Boolean) {
        val x1 = min(startPosition.left, x)
        val y1 = min(startPosition.top, y)
        val x2 = max(startPosition.left, x)
        val y2 = max(startPosition.top, y)

        selectByRenderData(add) { renderData ->
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

        if (engine.trace) {
            engine.logger.info("selected ${selection.items.size} items in (${startPosition.left} ${startPosition.top}, $x,$y) additional: $add")
        }
    }

    /**
     * User inducted select, creates and executes an undoable select operation.
     */
    fun select(x: Double, y: Double, add: Boolean) {
        selectByRenderData(add) { it.contains(x, y) }
        if (engine.trace) {
            engine.logger.info("selected ${selection.items.size} at ($x,$y) additional: $add")
        }
    }

    private fun selectByRenderData(add: Boolean, condition: (renderData: AuiRenderData) -> Boolean) {

        val selectedItems = mutableListOf<SheetItem>()

        for (child in drawingLayer.layoutItems) {

            if (! condition(child.renderData)) continue

            val info = child.itemInfo() ?: continue

            selectedItems += items[info.index]
        }

        addSelectOp(selectedItems, add)
    }

    private fun addSelectOp(selectedItems: MutableList<SheetItem>, add: Boolean) {
        // if the selection is already up to date we don't have to refresh it
        // this prevents unnecessary undo/redo operations if the user clicks
        // on the same item a few times

        selectedItems.sortBy { it.index }
        if (selectedItems == selection.items) return

        if (add) {
            val indices = selectedItems.map { it.index }
            selection.items.forEach {
                if (it.index in indices) return@forEach
                selectedItems += it
            }
        }

        // we have to use select so it is possible to undo/redo
        this += Select(selectedItems)
    }

    private fun AuiRenderData.contains(x: Double, y: Double) =
        when {
            y < finalTop -> false
            x < finalLeft -> false
            x >= finalLeft + finalWidth -> false
            y >= finalTop + finalHeight -> false
            else -> true
        }

    private fun AdaptiveFragment?.itemInfo(): ItemInfo? {
        var current = this

        while (current != null) {
            val info = current.instructions.firstInstanceOfOrNull<ItemInfo>()
            if (info != null) return info

            current = current.parent
            if (current == drawingLayer) return null
        }

        return null
    }

    inline fun forSelection(action: (item: SheetItem) -> Unit) {
        selection.items.forEach { item ->
            action(item)
        }
    }

}