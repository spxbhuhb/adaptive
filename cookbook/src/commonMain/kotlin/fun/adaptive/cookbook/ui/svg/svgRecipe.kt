package `fun`.adaptive.cookbook.ui.svg

import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.frame_inspect
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colSpan
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.iconColors
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun svgRecipe(): AdaptiveFragment {
    grid {
        gap { 16.dp } .. maxWidth
        colTemplate(64.dp, 1.fr) .. rowTemplate(32.dp repeat 3)

        svg(Graphics.eco)
        text(" - default")

        svg(Graphics.eco) .. iconColors.onSurfaceFriendly
        text(" - with color")

        colorChange()

        box {
            colSpan(2)
            big()
        }
    }

    return fragment()
}

@Adaptive
private fun colorChange() {
    val random = poll(1.seconds) { now().epochSeconds.toInt() % 4 } ?: 0
    val colors = arrayOf(iconColors.onSurface, iconColors.onSurfaceVariant, iconColors.onSurfaceFriendly, iconColors.onSurfaceAngry)

    svg(Graphics.eco, traceAll) .. colors[random]

    text(" - with changing color - $random")
}

@Adaptive
private fun big() {
    column {
        gap { 16.dp }
        svg(Graphics.eco) .. svgHeight(128.dp) .. svgWidth(128.dp) .. size(128.dp, 128.dp)
        svg(Graphics.frame_inspect) .. svgHeight(128.dp) .. svgWidth(128.dp) .. size(128.dp, 128.dp)
    }
}