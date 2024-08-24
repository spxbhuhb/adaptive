package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.fragment.layout.RawBorder
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.Surrounding
import `fun`.adaptive.ui.render.decoration
import `fun`.adaptive.ui.render.layout

@Adat
class Border(
    val color: Color?,
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : AdaptiveInstruction, Surrounding {

    override fun apply(subject: Any) {
        // decided to duplicate the border to keep layout calculations separate from decorations
        layout(subject) {
            it.border = RawSurrounding(this, it.border ?: RawSurrounding.Companion.ZERO, it.adapter)
        }
        decoration(subject) {
            it.border = RawBorder(this, it.border ?: RawBorder.Companion.NONE, it.adapter)
        }
    }

    companion object {
        val NONE = Border(null, DPixel.Companion.ZERO, DPixel.Companion.ZERO, DPixel.Companion.ZERO, DPixel.Companion.ZERO)
    }
}