package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.foundation.internal.cleanStateMask
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Position

/**
 * @property   x   The raw [x] coordinate where the event happened, relative to the frame of the
 *                 fragment that the event handler is attached to.
 *
 * @property   y   The raw [y] coordinate where the event happened, relative to the frame of the
 *                 fragment that the event handler is attached to.
 */
class UIEvent(
    val fragment: AbstractAuiFragment<*>,
    val nativeEvent: Any?,
    val x: Double = Double.NaN,
    val y: Double = Double.NaN
) {
    val position: Position
        get() = Position(
            fragment.uiAdapter.toDp(y),
            fragment.uiAdapter.toDp(x)
        )

    fun patchIfDirty() {
        val closureOwner = fragment.createClosure.owner
        closureOwner.closePatchBatch()
    }
}