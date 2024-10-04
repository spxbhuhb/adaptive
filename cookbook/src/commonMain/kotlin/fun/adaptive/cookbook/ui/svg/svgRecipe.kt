package `fun`.adaptive.cookbook.ui.svg

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.iconColors
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun svgRecipe() {
    grid {
        gap { 16.dp } .. maxWidth
        colTemplate(64.dp, 1.fr) .. rowTemplate(32.dp repeat 3)

        svg(Res.drawable.eco)
        text(" - default")

        svg(Res.drawable.eco) .. iconColors.onSurfaceFriendly
        text(" - with color")

        colorChange()
    }
}

@Adaptive
private fun colorChange() {
    val random = poll(1.seconds) { now().epochSeconds.toInt() % 4 } ?: 0
    val colors = arrayOf(iconColors.onSurface, iconColors.onSurfaceVariant, iconColors.onSurfaceFriendly, iconColors.onSurfaceAngry)

    svg(Res.drawable.eco) .. colors[random]

    text(" - with changing color - $random")
}