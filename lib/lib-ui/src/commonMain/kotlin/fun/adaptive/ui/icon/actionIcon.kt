package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*

@Adaptive
fun actionIcon(
    icon: GraphicsResourceSet,
    tooltip: String? = null,
    theme: IconTheme = onSurfaceIconTheme,
): AdaptiveFragment {
    val hover = hover()
    val background = theme.background(hover)
    val svgColors = theme.svgColors(hover)

    box(theme.actionIconContainer, background, instructions()) {
        svg(icon, theme.actionIcon, svgColors, alignSelf.center)
        if (tooltip != null) {
            hoverPopup(theme.tooltip) {
                text(tooltip) .. theme.tooltipText
            }
        }
    }

    return fragment()
}