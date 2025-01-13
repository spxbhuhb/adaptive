package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.fragment.layout.RawDropShadow
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.decoration

@Adat
class DropShadow(
    val color: Color,
    val offsetX: DPixel,
    val offsetY: DPixel,
    val standardDeviation: DPixel
) : AdaptiveInstruction {
    override fun applyTo(subject: Any) {
        decoration(subject) { it.dropShadow = RawDropShadow(this, it.adapter) }
    }
}