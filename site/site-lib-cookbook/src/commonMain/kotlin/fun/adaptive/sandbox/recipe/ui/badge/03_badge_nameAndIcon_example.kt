package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.instruction.dp

/**
 * # With name and icon
 *
 * - Pass an icon in the `icon` parameter.
 */
@Adaptive
fun badgeNameAndIcon(): AdaptiveFragment {

    flowBox {
        gap { 8.dp }

        badge("suppressed", Graphics.marker)
        badge("success", Graphics.success, theme = BadgeTheme.success)
        badge("info", Graphics.info, theme = BadgeTheme.info)
        badge("warning", Graphics.warning, theme = BadgeTheme.warning)
        badge("error", Graphics.error, theme = BadgeTheme.error)
        badge("important", Graphics.power_settings_new, theme = BadgeTheme.important)
    }

    return fragment()
}