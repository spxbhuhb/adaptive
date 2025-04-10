package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.decoration

@Adat
class Cursor(
    val type: CursorType
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        decoration(subject) { it.cursor = type }
    }

    companion object {
        val pointer = Cursor(CursorType.Pointer)
        val colResize = Cursor(CursorType.ColResize)
        val rowResize = Cursor(CursorType.RowResize)
    }
}