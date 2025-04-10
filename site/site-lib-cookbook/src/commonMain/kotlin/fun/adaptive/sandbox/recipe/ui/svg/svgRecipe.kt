package `fun`.adaptive.sandbox.recipe.ui.svg

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.iconColors
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun svgRecipe(): AdaptiveFragment {
    grid {
        gap { 16.dp } .. maxSize .. verticalScroll
        colTemplate(64.dp, 1.fr) .. rowTemplate(32.dp repeat 3)

        svg(Graphics.account_circle)
        text(" - default")

        svg(Graphics.account_circle) .. iconColors.onSurfaceFriendly
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

    svg(Graphics.account_circle) .. colors[random]

    text(" - with changing color - $random")
}

@Adaptive
private fun big() {
    column {
        gap { 16.dp }
        svg(Graphics.account_circle) .. svgHeight(128.dp) .. svgWidth(128.dp) .. size(128.dp, 128.dp)
        svg(Graphics.settings) .. svgHeight(128.dp) .. svgWidth(128.dp) .. size(128.dp, 128.dp)
    }
}