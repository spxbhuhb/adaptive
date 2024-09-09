package `fun`.adaptive.grove.designer.utility

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.replaceWith
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlin.math.max
import kotlin.math.min

fun emptySelection() = Selection(emptyList(), 0)

fun selectionOf(event: UIEvent): Selection =
    Selection(hits(event.fragment, event.y, event.x), 0)

@Adat
class Selection(
    val items: List<AbstractAuiFragment<*>>,
    val revision: Int
) {

    fun isEmpty() = items.isEmpty()

    fun isNotEmpty() = items.isNotEmpty()

    operator fun contains(fragment: AbstractAuiFragment<*>): Boolean =
        items.contains(fragment)

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

    fun place(target: Selection) {
        if (target.isEmpty()) return

        val container = target.items.lastOrNull { it is AbstractContainer<*, *> } ?: return
        if (container in items) return

        for (item in items) {
            place(item, container as AbstractContainer<*, *>)
        }
    }

    private fun place(item: AbstractAuiFragment<*>, container: AbstractContainer<*, *>) {
        val instructions = item.instructions
        val result = instructions.toMutableList()

        val frame = instructions.firstOrNullIfInstance<Frame>()
        if (frame != null) {
            result.removeAll { it is Frame || it is Position }
            result += Size(frame.width, frame.height)
        } else {
            val position = instructions.firstOrNullIfInstance<Position>()
            if (position != null) {
                result.removeAll { it is Position }
            }
        }

        item.unmount()

        item.parent?.children?.remove(item)
        item.parent = container

        item.setStateVariable(item.instructionIndex, result.toTypedArray())
        item.setDirty(item.instructionIndex, true)

        item.mount()
    }

}