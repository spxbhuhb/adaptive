package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.layout.Position

/**
 * For all positional information ([x], [y], [position], [rawPosition]):
 *
 * - values are relative to the frame of the fragment that the event handler is attached to
 * - the fragment frame includes all surroundings: margin, border and padding
 * - mouse events over margin are **NOT** reported
 *
 * Note: when margin is present, the positional information never reaches zero.
 *
 * @property   position The device-independent position where the event happened.
 * @property   x   The raw [x] coordinate where the event happened, see [UIEvent] for details.
 * @property   y   The raw [y] coordinate where the event happened, see [UIEvent] for details.
 */
class UIEvent(
    val fragment: AbstractAuiFragment<*>,
    val nativeEvent: Any?,
    val x: Double = Double.NaN,
    val y: Double = Double.NaN,
    val transferData: TransferData? = null,
    val keyInfo : KeyInfo? = null,
    val modifiers: Set<EventModifier> = emptySet(),
    val stopPropagation: () -> Unit = { }
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

    class KeyInfo(
        val key: String,
        val isComposing: Boolean,
        val repeat: Boolean
    )
}