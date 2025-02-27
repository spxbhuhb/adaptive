package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.grove.sheet.model.*
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent.KeyInfo
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.*
import kotlin.math.abs
import kotlin.time.measureTime

open class SheetViewController(
    val printTrace: Boolean = false,
    val recordOperations: Boolean = false,
    val recordMerge: Boolean = false
) {
    companion object {
        fun AdaptiveFragment.sheetViewController() : SheetViewController =
            findContext<SheetViewController>()!!
    }

    val logger = getLogger("SheetViewController").also { if (printTrace) it.enableFine() }

    val groupUuid = UUID<LfmDescendant>("db7b3633-c0f7-479d-812d-837d80065dfb")

    val models = mutableMapOf<UUID<LfmDescendant>, LfmDescendant>(
        groupUuid to LfmDescendant("aui:rectangle", emptyInstructions, uuid = groupUuid)
    )

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

    /**
     * I'm not particularly happy with this solution as it feels wrong to generate
     * in [items] without adding an actual item. However, we need this to make
     * index mapping easier. Let's be _very_ careful when using [nextIndex].
     */
    private var nextIndex = ItemIndex(- 1)
        get() {
            field = ItemIndex(field.value + 1)
            return field
        }
        set(_) {
            throw UnsupportedOperationException()
        }

    var selection = emptySelection
        private set(value) {
            field = value
            selectionStore.value = value
            controlFrameStore.value = value.containingFrame.toFrame(drawingLayer.uiAdapter).grow(8.0)
        }

    val selectionFrame: Frame
        get() = selection.containingFrame.toFrame(drawingLayer.uiAdapter)

    val selectionStore = storeFor { selection }

    var clipboard = SheetClipboard(emptyList())

    val undoStack: Stack<SheetOperation> = mutableListOf<SheetOperation>()

    val redoStack: Stack<SheetOperation> = mutableListOf<SheetOperation>()

    val snapshot
        get() = SheetSnapshot(
            models.values.toList(),
            items.mapNotNull { if (it.removed) null else makeClipboardItem(it) },
            recording.toList()
        )

    val recording = mutableListOf<SheetOperation>()

    lateinit var drawingLayer: GroveDrawingLayer<*, *>

    // --------------------------------------------------------------------------------
    // Controller data
    // --------------------------------------------------------------------------------

    /**
     * The frame that contains the controls for the current selection. This is typically
     * larger than [selectionFrame] because it also contains the controls.
     */
    var controlFrame: Frame
        get() = controlFrameStore.value
        private set(v) {
            controlFrameStore.value = v
        }

    var controlFrameStore = storeFor { Frame.NaF }

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

    val multiplierStore = storeFor { 0 }

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
        if (recordOperations) recording += Undo()

        val last = undoStack.popOrNull() ?: return
        if (printTrace) logger.fine { "UNDO -- $last" }
        last.revert(this@SheetViewController)
        redoStack.push(last)
    }

    fun redo() {
        if (recordOperations) recording += Redo()

        val last = redoStack.popOrNull() ?: return
        if (printTrace) logger.fine { "REDO -- $last" }
        last.commit(this@SheetViewController)
        undoStack.push(last)
    }

    fun op(operation: SheetOperation) {
        val result: OperationResult
        measureTime {
            result = operation.commit(this@SheetViewController)

            when (result) {
                OperationResult.PUSH -> {
                    if (recordOperations) {
                        recording += operation
                    }
                    undoStack.push(operation)
                }

                OperationResult.REPLACE -> {
                    if (recordOperations) {
                        if (recordMerge) {
                            recording[recording.lastIndex] = operation
                        } else {
                            recording += operation
                        }
                    }
                    undoStack.pop()
                    undoStack.push(operation)
                }

                OperationResult.DROP -> Unit
            }

            redoStack.clear()

            operation.firstRun = false

        }.also {
            if (printTrace) logger.fine { "$it - $operation $result" }
        }
    }

    // --------------------------------------------------------------------------------
    // Item lifecycle
    // --------------------------------------------------------------------------------

    fun indexMapFor(clipboardItems: List<SheetClipboardItem>): Map<ClipboardIndex, ItemIndex> =
        clipboardItems.mapIndexed { index, clipboardItem -> clipboardItem.clipboardIndex to nextIndex }.toMap()

    fun createItem(
        modelUuid: UUID<LfmDescendant>,
        position: Position,
        size: Size? = null,
        name: String? = null
    ): SheetItem {

        val model = models[modelUuid] ?: error("Model not found: $modelUuid")

        val clipboardItem =
            SheetClipboardItem(
                clipboardIndex = - 1,
                name = name ?: model.key,
                model = modelUuid,
                instructions = null,
                group = null,
                members = null
            )

        val item = createItem(
            clipboardItem,
            position,
            size,
            indexMapFor(listOf(clipboardItem))
        )

        return item
    }

    fun createItem(
        clipboardItem: SheetClipboardItem,
        position: Position?,
        size: Size?,
        indexMap: Map<ClipboardIndex, ItemIndex>
    ): SheetItem {

        val index = indexMap[clipboardItem.clipboardIndex] !!

        val model = models[clipboardItem.model] ?: error("Model not found: ${clipboardItem.model}")
        val originalInstructions = clipboardItem.instructions ?: model.instructions

        val position = position ?: originalInstructions.lastInstanceOfOrNull<Position>() ?: Position(10.dp, 10.dp)
        val size = size ?: originalInstructions.lastInstanceOfOrNull<Size>()

        val otherInstructions = originalInstructions.removeAll { it is Position || it is Size || it is ItemInfo }

        val instanceInstructions = if (size == null) {
            instructionsOf(otherInstructions, ItemInfo(index.value), position)
        } else {
            instructionsOf(otherInstructions, ItemInfo(index.value), position, size)
        }

        SheetItem(
            index,
            clipboardItem.name,
            model.uuid
        ).also {
            it.initialInstructions = instanceInstructions
            it.group = clipboardItem.group?.let { indexMap[it] !! }
            it.members = clipboardItem.members?.map { indexMap[it] !! }?.toMutableList()
            items += it
            return it
        }
    }

    fun showItem(item: SheetItem) {
        showItem(item.index)
    }

    fun showItem(index: ItemIndex) {
        val item = items[index.value]

        val beforeRemove = item.beforeRemove

        if (beforeRemove != null) {

            if (beforeRemove.group != null) {
                val groupIndex = ItemIndex(beforeRemove.group)
                item.group = groupIndex
                items[groupIndex.value].members?.let { it += index }
            }

            if (beforeRemove.members != null) {
                val members = beforeRemove.members.map { ItemIndex(it) }.toMutableList()
                item.members = members
                members.forEach {
                    items[it.value].group = item.index
                }
            }

        }

        drawingLayer += item

        item.removed = false
        item.beforeRemove = null // this MUST be after drawingLayer.plusAssign
    }

    fun hideItem(item: SheetItem) {
        hideItem(item.index)
    }

    fun hideItem(index: ItemIndex) {
        val item = items[index.value]
        if (item.removed) return

        item.beforeRemove = makeClipboardItem(item)
        item.removed = true

        drawingLayer -= item

        item.group = null
        item.members = null
    }

    // --------------------------------------------------------------------------------
    // Select
    // --------------------------------------------------------------------------------

    fun selectionToClipboard(): SheetClipboard {
        val out = mutableListOf<SheetClipboardItem>()

        forSelection {
            out += makeClipboardItem(it)
        }

        return SheetClipboard(out)
    }

    private fun makeClipboardItem(item: SheetItem) =
        SheetClipboardItem(
            clipboardIndex = item.index.value,
            name = item.name,
            model = item.model,
            instructions = item.fragment.instructions,
            group = item.group?.value,
            members = item.members?.map { it.value }
        )

    fun forSelection(action: (item: SheetItem) -> Unit) {
        selection.items.forEach { item ->
            action(item)
        }
    }

    /**
     * Direct version of select which is not put into the undo stack.
     */
    fun select() {
        selection = emptySelection
    }

    /**
     * Direct version of select which is not put into the undo stack.
     */
    fun select(
        selectedItems: List<SheetItem>,
        additional: Boolean,
        containingFrame: RawFrame? = null
    ) {
        // Transforms (Move, Resize) use these settings.
        // For these it is important to be fast, so it's better to have a shortcut here.
        if (! additional && containingFrame != null) {
            selection = SheetSelection(selectedItems, containingFrame)
            return
        }

        val result = selectedItems.toMutableList()

        if (additional) {
            val indices = result.map { it.index.value }

            selection.items.forEach {
                if (it.index.value in indices) return@forEach
                result += it
            }

            // if the selection is already up to date we don't have to refresh it
            // this prevents unnecessary undo/redo operations if the user clicks
            // on the same item a few times

            result.sortBy { it.index.value }
            if (result == selection.items) return
        }

        selection = SheetSelection(result, containingFrame ?: SheetSelection.containingFrame(result))
    }

    /**
     * Direct version of select which is not put into the undo stack.
     */
    fun select(index: ItemIndex) {
        select(listOf(items[index.value]), false)
    }

    fun findByRenderData(condition: (renderData: AuiRenderData) -> Boolean): MutableList<SheetItem> {

        val selectedItems = mutableListOf<SheetItem>()

        for (child in drawingLayer.layoutItems) {

            if (! condition(child.renderData)) continue

            val info = child.itemInfo() ?: continue

            val item = items[info.index]
            if (item.isGroup) continue
            if (item.removed) continue
            if (item.isInGroup) {
                selectGroup(selectedItems, item.group !!)
            } else {
                selectedItems += item
            }
        }

        return selectedItems
    }

    private fun selectGroup(selectedItems: MutableList<SheetItem>, group: ItemIndex) {
        val group = items[group.value]
        if (group.isInGroup) {
            selectGroup(selectedItems, group.group !!)
        } else {
            selectedItems += group
        }
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

    fun toFrame(rawFrame: RawFrame) =
        rawFrame.toFrame(drawingLayer.uiAdapter)

    fun toRawFrame(top: DPixel, left: DPixel, width: DPixel, height: DPixel) =
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

    fun onTransformStart(position: Position, additional: Boolean) {
        startPosition = position
        lastPosition = position
        transformStart = vmNowMicro()

        if (position !in controlFrame) {
            this += SelectByPosition(position, additional)
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
            controlFrame = Frame.NaF // this must be before SelectByFrame or it will clear the frame
            this += SelectByFrame(Frame(startPosition, position), add)
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