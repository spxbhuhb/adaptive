package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.layout

class Overflow(
    val overflow: OverflowBehavior?
) : AdaptiveInstruction {

    override fun applyTo(subject: Any) {
        layout(subject) {
            it.overflow = overflow
        }
    }

    companion object {
        val visible = Overflow(OverflowBehavior.Visible)
        val hidden = Overflow(OverflowBehavior.Hidden)
        val scroll = Overflow(OverflowBehavior.Scroll)
        val auto = Overflow(OverflowBehavior.Auto)
        val clip = Overflow(OverflowBehavior.Clip)
    }
}