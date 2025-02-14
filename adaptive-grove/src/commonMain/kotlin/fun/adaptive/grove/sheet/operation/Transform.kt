package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemIndex
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
abstract class Transform: SheetOperation() {

    abstract val start: Long
    abstract var transformX : DPixel
    abstract var transformY : DPixel
    abstract val withSizes: Boolean

    var originalSelection = emptySelection
    var originalInstructions = mutableMapOf<ItemIndex, AdaptiveInstructionGroup>()

    var startFrame = RawFrame.NaF

    var itemCache = mutableListOf<SheetItem>()
    var instructionCache = mutableListOf<AdaptiveInstructionGroup>()
    var positionCache = mutableListOf<Position>()
    var sizeCache = mutableListOf<Size>()

    override fun commit(controller: SheetViewController): OperationResult {
        if (controller.selection.isEmpty()) {
            return OperationResult.DROP
        }

        val last = controller.undoStack.lastOrNull()
        val merge = (last != null && last::class === this::class && last is Transform && last.start == start)

        if (merge) {
            merge(last)
        } else {
            initialize(controller)
        }

        itemCache.forEachIndexed { cacheIndex, item ->
            item.applyInstructions(controller, newInstructions(cacheIndex))
        }

        controller.select(
            originalSelection.items,
            additional = false,
            newFrame(controller)
        )

        return if (merge) OperationResult.REPLACE else OperationResult.PUSH
    }

    open fun initialize(controller: SheetViewController) {
        startFrame = controller.selection.containingFrame
        originalSelection = controller.selection
        originalSelection.forItems {
            preprocess(controller, it)
        }
    }

    fun preprocess(controller: SheetViewController, item: SheetItem) {
        val instructions = item.fragment.instructions

        originalInstructions[item.index] = instructions

        itemCache += item
        instructionCache += instructions.removeAll { it is Position || (withSizes && it is Size) }
        positionCache += instructions.firstInstanceOf<Position>()

        if (withSizes) {
            sizeCache += instructions.firstInstanceOf<Size>()
        }

        if (item.isGroup) {
            item.members !!.forEach { preprocess(controller, controller.items[it.value]) }
        }
    }

    open fun merge(last: Transform) {
        originalSelection = last.originalSelection
        originalInstructions = last.originalInstructions
        startFrame = last.startFrame
        itemCache = last.itemCache
        instructionCache = last.instructionCache
        positionCache = last.positionCache
        sizeCache = last.sizeCache
        transformX += last.transformX
        transformY += last.transformY
    }

    abstract fun newFrame(controller: SheetViewController): RawFrame

    abstract fun newInstructions(cacheIndex: Int): AdaptiveInstruction

    fun SheetItem.applyInstructions(controller: SheetViewController, instructions: AdaptiveInstruction) {
        fragment.apply {
            setStateVariable(0, instructions)
            genPatchInternal()
            controller.updateLayout(this@applyInstructions)
        }
    }

    override fun revert(controller: SheetViewController) {
        originalSelection.items.forEach { item ->
            val originalInstructions = originalInstructions[item.index] ?: return@forEach
            item.applyInstructions(controller, originalInstructions)
        }
        controller.select(originalSelection.items, additional = false)
    }

}