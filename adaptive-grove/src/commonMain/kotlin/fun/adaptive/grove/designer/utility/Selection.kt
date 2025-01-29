package `fun`.adaptive.grove.designer.utility

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.utility.firstOrNullIfInstance

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

    fun containingFrame(selection: Selection): Frame? =
        TODO()

    fun move(previousCursorPosition: Position, currentCursorPosition: Position) {
        if (items.isEmpty()) return

        val dy = currentCursorPosition.top.value - previousCursorPosition.top.value
        val dx = currentCursorPosition.left.value - previousCursorPosition.left.value

        val item = items.last() // FIXME we don't want to move layouts???
        val result = item.instructions.toMutableList()

        val frame = result.firstOrNullIfInstance<Frame>()

        if (frame != null) {
            result.removeAll { it is Frame || it is Position }
            result += Frame(frame.top + dy, frame.left + dx, frame.width, frame.height)
        } else {

            val position = result.firstOrNullIfInstance<Position>()

            if (position != null) {
                result.removeAll { it is Position }
                result += Position(position.top + dy, position.left + dx)
            } else {
                result += Position(currentCursorPosition.top, currentCursorPosition.left)
            }
        }

        item.setStateVariable(0, AdaptiveInstructionGroup(result))

        nextRevision()
    }

    fun nextRevision() {
        this.update(Selection(items, revision + 1))
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

        val frame = instructions.firstInstanceOfOrNull<Frame>()
        if (frame != null) {
            result.removeAll { it is Frame || it is Position }
            result += Size(frame.width, frame.height)
        } else {
            val position = instructions.firstInstanceOfOrNull<Position>()
            if (position != null) {
                result.removeAll { it is Position }
            }
        }

        item.unmount()

        item.parent?.children?.remove(item)
        item.parent = container

        item.setStateVariable(0, AdaptiveInstructionGroup(result))
        item.setDirtyBatch(0)

        item.mount()
    }

}