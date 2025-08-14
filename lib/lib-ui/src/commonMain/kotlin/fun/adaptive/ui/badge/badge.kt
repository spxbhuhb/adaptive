package `fun`.adaptive.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun badge(
    name: String,
    icon: GraphicsResourceSet? = null,
    removable : Boolean = false,
    theme: BadgeTheme = BadgeTheme.default,
    useSeverity: Boolean = false,
    translate : Boolean = true,
    removeFun: ((name : String) -> Unit)? = null
) : AdaptiveFragment {

    val effectiveTheme = if (useSeverity) {
        badgeThemeMap[name] ?: theme
    } else {
        theme
    }

    val effectiveIcon = icon ?: if (useSeverity) effectiveTheme.iconResource else null

    val containerInst = when {
        effectiveIcon != null && removable -> effectiveTheme.textContainerIconAndRemovable
        effectiveIcon != null -> effectiveTheme.textContainerIcon
        removable -> effectiveTheme.textContainerRemovable
        else -> effectiveTheme.textContainerStandalone
    }

    row(instructions()) {
        effectiveTheme.outerContainer
        if (effectiveIcon != null) {
            box {
                effectiveTheme.iconContainer
                icon(effectiveIcon) .. effectiveTheme.icon
            }
        }
        box {
            containerInst
            text(if (translate) fragment().resolveString(name) else name) .. effectiveTheme.text
        }
        if (removable && removeFun != null) {
            removableIcon(effectiveTheme, name, removeFun)
        }
    }

    return fragment()
}

@Adaptive
private fun removableIcon(
    theme: BadgeTheme,
    name: String,
    removeFun: (String) -> Unit
) {
    val hover = hover()

    box {
        onClick { removeFun(name) }
        if (hover) theme.removableContainerHover else theme.removableContainer
        icon(Graphics.close) .. if (hover) theme.removableIconHover else theme.removableIcon
    }
}