package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size

/**
 * @property  start  The VM time when the move operation started. The operation uses this time to merge
 *                   with previous move operations.
 */
abstract class Transform(
    val start: Long,
    val deltaX: DPixel,
    val deltaY: DPixel,
    val withSizes: Boolean
) : SheetOperation() {

    var transformX = deltaX
    var transformY = deltaY

    var undoData = emptyMap<Int, AdaptiveInstructionGroup>()

    var startFrame = RawFrame.NaF

    var instructionCache = emptyList<AdaptiveInstructionGroup>()
    var positionCache = emptyList<Position>()
    var sizeCache = emptyList<Size>()

    override fun commit(controller: SheetViewController): Boolean {
        val last = controller.undoStack.lastOrNull()
        val merge = (last != null && last::class === this::class && last is Transform && last.start == start)

        if (merge) {
            merge(last)
        } else {
            initialize(controller)
        }

        controller.selection.items.forEachIndexed { index, item ->
            item.fragment.apply {
                setStateVariable(0, newInstructions(index))
                genPatchInternal()
            }
        }

        controller.select(
            controller.selection.items,
            newFrame(controller)
        )

        return merge
    }

    open fun initialize(controller: SheetViewController) {

        startFrame = controller.selection.containingFrame

        val undoData = mutableMapOf<Int, AdaptiveInstructionGroup>()
        val instructionCache = mutableListOf<AdaptiveInstructionGroup>()
        val positionCache = mutableListOf<Position>()
        val sizeCache = mutableListOf<Size>()

        controller.selection.items.forEachIndexed { index, item ->
            val instructions = item.fragment.instructions

            undoData[index] = instructions
            instructionCache += instructions.removeAll { it is Position || (withSizes && it is Size) }
            positionCache += instructions.firstInstanceOf<Position>()
            if (withSizes) {
                sizeCache += instructions.firstInstanceOf<Size>()
            }
        }

        this.undoData = undoData
        this.instructionCache = instructionCache
        this.positionCache = positionCache
        this.sizeCache = sizeCache
    }

    open fun merge(last: Transform) {
        undoData = last.undoData
        startFrame = last.startFrame
        instructionCache = last.instructionCache
        positionCache = last.positionCache
        sizeCache = last.sizeCache
        transformX += last.transformX
        transformY += last.transformY
    }

    abstract fun newFrame(controller: SheetViewController): RawFrame

    abstract fun newInstructions(cacheIndex : Int): AdaptiveInstructionGroup

    override fun revert(controller: SheetViewController) {
        controller.forSelection { item ->
            val originalInstructions = undoData[item.index] ?: return@forSelection
            val fragment = item.fragment
            fragment.setStateVariable(0, originalInstructions)
            fragment.genPatchInternal()
            controller.updateLayout(item)
        }
        controller.select(controller.selection.items)
    }

}