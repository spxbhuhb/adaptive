package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.theme.iconColors

@Adaptive
fun friendlyOrAngry(
    condition: Boolean,
    icon1: GraphicsResourceSet,
    icon2: GraphicsResourceSet,
    instruction1: AdaptiveInstruction = iconColors.onSurfaceFriendly,
    instruction2: AdaptiveInstruction = iconColors.onSurfaceAngry,
) {
    box {
        if (condition) {
            icon(icon1, instruction1)
        } else {
            icon(icon2, instruction2)
        }
    }
}