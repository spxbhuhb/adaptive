package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.decoration

@Adat
class CornerRadius(
    val topLeft: DPixel?,
    val topRight: DPixel?,
    val bottomLeft: DPixel?,
    val bottomRight: DPixel?
) : AdaptiveInstruction {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        decoration(subject) {
            it.cornerRadius = RawCornerRadius(this, it.cornerRadius ?: RawCornerRadius.Companion.ZERO, it.adapter)
        }
    }

}