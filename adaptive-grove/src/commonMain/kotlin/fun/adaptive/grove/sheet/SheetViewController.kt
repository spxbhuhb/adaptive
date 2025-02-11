package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.adaptiveValueStore
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.grove.sheet.model.*
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent.KeyInfo
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

class SheetViewController(
    val trace: Boolean
) {
    val logger = getLogger("SheetViewController").also { if (trace) it.enableFine() }

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

    private val nextIndex
        get() = items.size

    var selection = emptySelection
        private set(value) {
            field = value
            selectionStore.value = value
            controlFrameStore.value = value.containingFrame.toFrame(drawingLayer.uiAdapter).grow(8.0)
        }

    val selectionStore = adaptiveValueStore { selection }

    var clipboard = SheetClipboard(emptyList(), emptyList())

    val undoStack: Stack<SheetOperation> = mutableListOf<SheetOperation>()

    val redoStack: Stack<SheetOperation> = mutableListOf<SheetOperation>()

    lateinit var drawingLayer: GroveDrawingLayer<*, *>

    // --------------------------------------------------------------------------------
    // Controller data
    // --------------------------------------------------------------------------------

    /**
     * The frame that contains the current selection.
     */
    var controlFrame: Frame
        get() = controlFrameStore.value
        private set(v) {
            controlFrameStore.value = v
        }

    var controlFrameStore = adaptiveValueStore { Frame.NaF }

    /**
     * Name of the active control for an ongoing continuous operation such as resize.
     * Default control names can be found in `controlLayer.ControlNames`.
     */
    var activeHandle: HandleInfo? = null

    /**
     * When not zero, the next action initiated with keyboard will be run this many
     * times. For example typing 10 Ctrl-V will paste the clipboard 10 times.
     */
    var multiplier: Int
        get() = multiplierStore.value
        private set(v) {
            multiplierStore.value = v
        }

    val multiplierStore = adaptiveValueStore { 0 }

    /**
     * The VM time when the current transform (move, resize etc.) operation started.
     * Used for merging operations together when there are multiple pointer events
     * for one logical operation.
     */
    var transformStart = 0L

    /**
     * The position where the transform operation started (position of `onPrimaryDown`).
     */
    var startPosition = Position.NaP

    /**
     * Position of the last event in a  transform operations (`onPrimaryMove` for example.)
     */
    var lastPosition = Position.NaP

    // --------------------------------------------------------------------------------
    // Execute operations, undo, redo
    // --------------------------------------------------------------------------------

    operator fun plusAssign(operation: SheetOperation) {
        execute(operation)
    }

    fun execute(operation: SheetOperation) {
        when (operation) {
            is Undo -> undo()
            is Redo -> redo()
            else -> op(operation)
        }
    }

    fun undo() {
        val last = undoStack.popOrNull() ?: return
        if (trace) logger.fine { "UNDO -- $last" }
        last.revert(this@SheetViewController)
        redoStack.push(last)
    }

    fun redo() {
        val last = redoStack.popOrNull() ?: return
        if (trace) logger.fine { "REDO -- $last" }
        last.commit(this@SheetViewController)
        undoStack.push(last)
    }

    fun op(operation: SheetOperation) {
        measureTime {
            val beforeSize = items.size

            val replace = operation.commit(this@SheetViewController)
            if (replace) undoStack.pop()
            undoStack.push(operation)
            redoStack.clear()

            val afterSize = items.size
            if (beforeSize != afterSize) {
                select(items.subList(beforeSize, afterSize))
            }

            operation.firstRun = false

        }.also {
            if (trace) logger.fine { "$it - $operation" }
        }
    }

    // --------------------------------------------------------------------------------
    // Item addition and removal
    // --------------------------------------------------------------------------------

    fun addItem(x: DPixel, y: DPixel, template: LfmDescendant): SheetItem {

        val index = nextIndex
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

    // --------------------------------------------------------------------------------
    // Select
    // --------------------------------------------------------------------------------

    fun selectionToClipboard(): SheetClipboard {
        val modelData = mutableListOf<LfmDescendant>()
        val frameData = mutableListOf<RawFrame>()

        forSelection {
            modelData += it.model
            frameData += it.frame
        }

        return SheetClipboard(modelData, frameData)
    }

    fun forSelection(action: (item: SheetItem) -> Unit) {
        selection.items.forEach { item ->
            action(item)
        }
    }

    fun <T> mapSelection(action: (item: SheetItem) -> T) : List<T> =
        selection.items.map { item ->
            action(item)
        }

    fun select() {
        selection = emptySelection
    }

    /**
     * Direct version of select which is not put into the undo stack.
     */
    fun select(items: List<SheetItem>, containingFrame: RawFrame = SheetSelection.containingFrame(items)) {
        if (trace) logger.info("selecting ${items.size} items")
        selection = SheetSelection(items, containingFrame)
    }

    fun select(item: SheetItem, add: Boolean) {
        addSelectOp(mutableListOf(item), add)
    }

    /**
     * User inducted select, creates and executes an undoable select operation.
     */
    fun selectByArea(p1: Position, p2: Position, add: Boolean) {

        val px1 = p1.left.px
        val py1 = p1.top.px
        val px2 = p2.left.px
        val py2 = p2.top.px

        val x1 = min(px1, px2)
        val y1 = min(py1, py2)
        val x2 = max(px1, px2)
        val y2 = max(py1, py2)

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

        if (trace) {
            logger.info("selected ${selection.items.size} items in $p1, $p2 additional: $add")
        }
    }

    /**
     * User inducted select, creates and executes an undoable select operation.
     */
    fun selectByPoint(position: Position, add: Boolean) {
        val x = position.left.px
        val y = position.top.px

        selectByRenderData(add) { it.contains(x, y) }

        if (trace) {
            logger.info("selected ${selection.items.size} at ($x,$y) additional: $add")
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

    private fun AdaptiveFragment?.itemInfo(): ItemInfo? {

        var current = this@itemInfo

        while (current != null) {
            val info = current.instructions.firstInstanceOfOrNull<ItemInfo>()
            if (info != null) return info

            current = current.parent
            if (current == drawingLayer) return null
        }

        return null
    }

    // --------------------------------------------------------------------------------
    // Drawing layer
    // --------------------------------------------------------------------------------

    fun updateLayout(item: SheetItem) {
        drawingLayer.updateLayout(item)
    }

    val DPixel.px
        inline get() = drawingLayer.toPx(this)

    fun toPx(dp: DPixel) =
        drawingLayer.toPx(dp)

    fun toFrame(rawFrame : RawFrame) =
        rawFrame.toFrame(drawingLayer.uiAdapter)

    fun toRawFrame(top : DPixel, left : DPixel, width : DPixel, height : DPixel) =
        RawFrame(
            top.px,
            left.px,
            width.px,
            height.px
        )

    // --------------------------------------------------------------------------------
    // Pointer event handling
    // --------------------------------------------------------------------------------

    fun isTransformActive(): Boolean =
        startPosition !== Position.NaP && lastPosition !== Position.NaP

    fun onTransformStart(position: Position, add: Boolean) {
        startPosition = position
        lastPosition = position
        transformStart = vmNowMicro()

        if (position !in controlFrame) {
            selectByPoint(position, add)
            return
        }
    }

    fun onTransformChange(position: Position) {
        // when start position is NaP the pointer movement started outside the window
        if (! isTransformActive()) return

        if (selection.isEmpty()) {
            val dx = abs(startPosition.left.value - position.left.value)
            val dy = abs(startPosition.top.value - position.top.value)

            if (dx >= 1.0 && dy >= 1.0) {
                controlFrame = Frame(startPosition, position)
            } else {
                controlFrame = Frame.NaF
            }
        } else {
            val dx = position.left - lastPosition.left
            val dy = position.top - lastPosition.top

            if (activeHandle == null) {
                this += Move(transformStart, dx, dy)
            } else {
                this += Resize(transformStart, dx, dy, activeHandle !!)
            }
        }

        lastPosition = position
    }

    fun onTransformEnd(position: Position, add: Boolean) {
        // when start position is NaP the pointer movement started outside the window
        if (selection.isEmpty() && startPosition !== Position.NaP) {
            controlFrame = Frame.NaF
            selectByArea(startPosition, position, add)
        }

        transformStart = 0L
        startPosition = Position.NaP
        lastPosition = Position.NaP
        activeHandle = null

    }

    // --------------------------------------------------------------------------------
    // Keyboard event handling
    // --------------------------------------------------------------------------------

    fun onKeyDown(keyInfo: KeyInfo, modifiers: Set<EventModifier>) {
        var keepMultiplier = false
        val effectiveMultiplier = multiplier.let { if (it == 0) 1 else it }

        when (keyInfo.key) {
            Keys.CONTROL -> keepMultiplier = true
            Keys.ALT -> keepMultiplier = true
            Keys.META -> keepMultiplier = true
            Keys.ALT_GRAPH -> keepMultiplier = true
            Keys.SHIFT -> keepMultiplier = true

            Keys.ESCAPE -> select()

            Keys.X -> this += Cut()

            Keys.C -> this += Copy()

            Keys.V -> this += Paste()

            Keys.Z -> if (EventModifier.CTRL in modifiers || EventModifier.META in modifiers) {
                if (EventModifier.SHIFT in modifiers) {
                    this += Redo()
                } else {
                    this += Undo()
                }
            }

            Keys.BACKSPACE -> this += Remove()
            Keys.DELETE -> this += Remove()

            Keys.ARROW_UP -> move(0.0, - 1.0 * effectiveMultiplier)
            Keys.ARROW_DOWN -> move(0.0, 1.0 * effectiveMultiplier)
            Keys.ARROW_LEFT -> move(- 1.0 * effectiveMultiplier, 0.0)
            Keys.ARROW_RIGHT -> move(1.0 * effectiveMultiplier, 0.0)

            else -> keepMultiplier = multiKeyHandler(keyInfo.key)
        }

        if (! keepMultiplier) {
            multiplier = 0
        }
    }

    private fun multiKeyHandler(key: String): Boolean {

        when {
            key.length == 1 && key.first().isDigit() -> {
                shiftMultiplier(key.toInt())
                return true
            }
        }

        return false
    }

    private fun shiftMultiplier(value: Int) {
        val current = multiplier
        if (current == 0) {
            multiplier = value
        } else {
            multiplier = current * 10 + value
        }
    }

    private fun move(deltaX: Double, deltaY: Double) {
        if (! selection.isEmpty()) this += Move(vmNowMicro(), deltaX.dp, deltaY.dp)
    }
}