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
    val colors = theme.colors(hover)

    box(theme.actionIconContainer, colors, instructions()) {
        svg(icon, theme.actionIcon, colors, instructions())
    }

    return fragment()
}