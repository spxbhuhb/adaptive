package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawPosition
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
    val y: Double = Double.NaN,
    val transferData: TransferData? = null,
    val modifiers: Set<EventModifier> = emptySet()
) {
    val position: Position
        get() = Position(
            fragment.uiAdapter.toDp(y),
            fragment.uiAdapter.toDp(x)
        )

    val rawPosition: RawPosition
        get() = RawPosition(y, x)

    operator fun contains(modifier: EventModifier): Boolean = modifier in modifiers

    fun patchIfDirty() {
        val closureOwner = fragment.createClosure.owner
        closureOwner.closePatchBatch()
    }
}