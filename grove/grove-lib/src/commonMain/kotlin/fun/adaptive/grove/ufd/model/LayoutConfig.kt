package `fun`.adaptive.grove.ufd.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Margin
import `fun`.adaptive.ui.instruction.layout.Padding
import `fun`.adaptive.ui.render.model.AuiRenderData

@Adat
class LayoutConfig(
    val top: Double = 0.0,
    val left: Double = 0.0,
    val width: Double = 0.0,
    val height: Double = 0.0,
    val margin: Margin = Margin.NONE,
    val padding: Padding = Padding.NONE
) {

    fun toInstructions(): AdaptiveInstructionGroup =
        instructionsOf(
            position(top.dp, left.dp),
            width { width.dp },
            height { height.dp },
            margin,
            padding
        )

    companion object {

        fun fromInstructions(instructions: AdaptiveInstructionGroup, adapter : AbstractAuiAdapter<*,*>): LayoutConfig {

            val renderData = AuiRenderData(adapter)
            instructions.applyTo(renderData)

            val layout = renderData.layout ?: return LayoutConfig()

            return LayoutConfig(
                layout.instructedTop ?: 0.0,
                layout.instructedLeft ?: 0.0,
                layout.instructedWidth ?: 0.0,
                layout.instructedHeight ?: 0.0,
                layout.margin?.let { Margin(it.top.dp, it.end.dp, it.bottom.dp, it.start.dp) } ?: Margin.NONE,
                layout.padding?.let { Padding(it.top.dp, it.end.dp, it.bottom.dp, it.start.dp) } ?: Padding.NONE
            )

        }

    }
}