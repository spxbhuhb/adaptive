package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.layout.Position

/**
 * For positions in [x], [y], [position], [rawPosition]:
 *
 * - values are relative to the frame of the fragment that the event handler is attached to
 * - the fragment frame includes all surroundings: margin, border and padding
 * - mouse events over margin are **NOT** reported
 *
 * Note: when margin is present, the positional information never reaches zero.
 *
 * For [clientX] and [clientY]:
 *
 * - value is relative to the main viewport of the application
 * - value is in device-dependent pixels
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
    val clientX: Double = Double.NaN,
    val clientY: Double = Double.NaN,
    val transferData: TransferData? = null,
    val keyInfo: KeyInfo? = null,
    val modifiers: Set<EventModifier> = emptySet(),
    val preventDefault: () -> Unit = { },
    val stopPropagation: () -> Unit = { },
    val releaseFocus: () -> Unit = { }
) {
    val position: Position
        get() = Position(
            fragment.uiAdapter.toDp(y),
            fragment.uiAdapter.toDp(x)
        )

    val rawPosition: RawPosition
        get() = RawPosition(y, x)

    val isEscape
        get() = keyInfo?.key == "Escape" || keyInfo?.key == "Esc"

    val isEnter
        get() = keyInfo?.key == "Enter"

    operator fun contains(modifier: EventModifier): Boolean = modifier in modifiers


    /**
     * Finds all descendants of [fragment] at the event position.
     *
     * @param verticalOnly If true, only consider the vertical position (x coordinate is ignored)
     * @param horizontalOnly If true, only consider the horizontal position (y coordinate is ignored)
     *
     * @return List of fragments found at the specified position
     */
    fun fragmentsByPosition(
        verticalOnly : Boolean = false,
        horizontalOnly : Boolean = false
    ): List<AbstractAuiFragment<*>> =
        fragment.uiAdapter.findByPosition(
            fragment,
            if (verticalOnly) Double.NaN else x,
            if (horizontalOnly) Double.NaN else y
        )

    fun patchIfDirty() {
        val closureOwner = fragment.createClosure.owner
        closureOwner.closePatchBatch()
    }

    class KeyInfo(
        val key: String,
        val isComposing: Boolean,
        val repeat: Boolean
    )

    fun acquirePointerCapture() {
        fragment.uiAdapter.acquirePointerCapture(this)
    }

    fun releasePointerCapture() {
        fragment.uiAdapter.releasePointerCapture(this)
    }

}