package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.DrawableResource

@Adaptive
fun icon(
    resource: DrawableResource,
    vararg instructions: AdaptiveInstruction,
    theme: IconTheme = onSurfaceIconTheme,
): AdaptiveFragment {
    svg(resource, *theme.icon, *instructions)
    return fragment()
}