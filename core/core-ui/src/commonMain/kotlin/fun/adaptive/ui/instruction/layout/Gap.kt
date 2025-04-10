package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.render.container
import `fun`.adaptive.ui.render.model.ContainerRenderData

@Adat
class Gap(
    val width: DPixel?,
    val height: DPixel?
) : AdaptiveInstruction {
    override fun applyTo(subject: Any) {
        container(subject) { c ->
            c.gapWidth = width?.let { c.adapter.toPx(it) } ?: c.gapWidth
            c.gapHeight = height?.let { c.adapter.toPx(it) } ?: c.gapHeight
        }
    }
}