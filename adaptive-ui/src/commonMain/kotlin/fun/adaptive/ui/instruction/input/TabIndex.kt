package `fun`.adaptive.ui.instruction.input

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.render.input

@Adat
class TabIndex(
    val value: Int
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        input(subject) {
            it.tabIndex = value
        }
    }

}