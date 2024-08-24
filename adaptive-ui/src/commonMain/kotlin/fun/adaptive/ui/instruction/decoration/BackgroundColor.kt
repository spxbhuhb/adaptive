package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.decoration

@Adat
class BackgroundColor(
    val color: Color
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        decoration(subject) { it.backgroundColor = color }
    }
}