package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.hover

@Adaptive
fun actionIcon(
    icon: GraphicsResourceSet,
    vararg instructions: AdaptiveInstruction,
    theme: IconTheme = onSurfaceIconTheme,
): AdaptiveFragment {
    val hover = hover()
    val background = theme.background(hover)
    val svgColors = theme.svgColors(hover)

    box(theme.actionIconContainer, background, instructions()) {
        svg(icon, theme.actionIcon, svgColors, instructions())
    }

    return fragment()
}