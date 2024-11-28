package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.hover

@Adaptive
fun actionIcon(
    resource: DrawableResource,
    vararg instructions: AdaptiveInstruction,
    theme: IconTheme = onSurfaceIconTheme,
): AdaptiveFragment {
    val hover = hover()
    val colors = theme.colors(hover)

    box(*theme.actionIconContainer, *colors, *instructions) {
        svg(resource, *theme.actionIcon, *colors, *instructions)
    }

    return fragment()
}