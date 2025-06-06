package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun badge(
    name : String,
    icon : GraphicsResourceSet? = null,
    theme : BadgeTheme = BadgeTheme.default
) {
    row {
        theme.outerContainer
        if (icon != null) {
            box {
                theme.iconContainer
                icon(icon) .. theme.icon
            }
        }
        box {
            theme.textContainer
            text(name) .. theme.text
        }
    }
}