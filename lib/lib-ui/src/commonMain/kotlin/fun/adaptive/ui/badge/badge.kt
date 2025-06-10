package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun badge(
    name: String,
    icon: GraphicsResourceSet? = null,
    theme: BadgeTheme = BadgeTheme.default,
    useSeverity: Boolean = false
) {
    val effectiveTheme = if (useSeverity) {
        badgeThemeMap[name] ?: theme
    } else {
        theme
    }

    val effectiveIcon = icon ?: if (useSeverity) effectiveTheme.iconResource else null

    row {
        effectiveTheme.outerContainer
        if (effectiveIcon != null) {
            box {
                effectiveTheme.iconContainer
                icon(effectiveIcon) .. effectiveTheme.icon
            }
        }
        box {
            effectiveTheme.textContainer
            text(name) .. effectiveTheme.text
        }
    }
}