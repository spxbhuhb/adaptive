package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*

@Adaptive
fun actionIcon(
    icon: GraphicsResourceSet,
    tooltip: String? = null,
    theme: IconTheme = onSurfaceIconTheme,
    onClickFun: (() -> Unit)? = null
): AdaptiveFragment {
    val hover = hover()
    val background = theme.background(hover)
    val svgColors = theme.svgColors(hover)

    box(theme.actionIconContainer, background) {
        svg(icon, theme.actionIcon, svgColors, alignSelf.center) ..
            if (onClickFun != null) instructions() + onClick { onClickFun.invoke() } else instructions()

        if (tooltip != null) {
            hoverPopup(theme.tooltip) {
                text(tooltip) .. theme.tooltipText
            }
        }
    }

    return fragment()
}