package hu.simplexion.adaptive.designer.utility

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.store.replaceWith
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Position
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.frame
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlin.math.max
import kotlin.math.min

@Adat
class Selection(
    val items: List<AbstractCommonFragment<*>>,
    val revision: Int = 0
) : AdatClass<Selection> {

    fun isEmpty() = items.isEmpty()

    fun isNotEmpty() = items.isNotEmpty()

    fun containingFrame(selection: Selection): Frame? {
        if (selection.items.isEmpty()) return null

        var top = Double.MAX_VALUE
        var left = Double.MAX_VALUE

        var right = Double.MIN_VALUE
        var bottom = Double.MIN_VALUE

        for (fragment in items) {
            val renderData = fragment.renderData
            val finalTop = renderData.finalTop
            val finalLeft = renderData.finalLeft

            top = min(top, finalTop)
            left = min(left, finalLeft)
            bottom = max(finalTop + renderData.finalHeight, bottom)
            right = max(finalLeft + renderData.finalWidth, left)
        }

        return frame(top.dp, left.dp, (right - left).dp, (bottom - top).dp)
    }

    fun move(previousCursorPosition: Position, currentCursorPosition: Position) {
        if (items.isEmpty()) return

        val dy = currentCursorPosition.top.value - previousCursorPosition.top.value
        val dx = currentCursorPosition.left.value - previousCursorPosition.left.value

        val item = items.last() // FIXME we don't want to move layouts???
        val instructions = item.instructions
        val result = instructions.toMutableList()

        val frame = instructions.firstOrNullIfInstance<Frame>()
        if (frame != null) {
            result.removeAll { it is Frame || it is Position }
            result += Frame(frame.top + dy, frame.left + dx, frame.width, frame.height)
        } else {
            val position = instructions.firstOrNullIfInstance<Position>()
            if (position != null) {
                result.removeAll { it is Position }
                result += Position(position.top + dy, position.left + dx)
            } else {
                result += Position(currentCursorPosition.top, currentCursorPosition.left)
            }
        }

        item.setStateVariable(item.instructionIndex, result.toTypedArray())
        item.setDirty(item.instructionIndex, true)
        nextRevision()
    }

    fun nextRevision() {
        this.replaceWith(Selection(items, revision + 1))
    }

}