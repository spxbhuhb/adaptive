package `fun`.adaptive.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svgOrImage
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

@Adaptive
fun icon(
    resource: GraphicsResourceSet,
    vararg instructions: AdaptiveInstruction,
    theme: IconTheme = onSurfaceIconTheme,
): AdaptiveFragment {
    svgOrImage(resource, theme.icon, instructions())
    return fragment()
}