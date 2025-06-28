package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.instruction.dp

/**
 * # With name only
 *
 * - Each badge has a `name`, this is the only mandatory parameter.
 * - The colors are defined by the theme, which you can pass in `theme`.
 * - Suppressed badge theme meant to display secondary information.
 */
@Adaptive
fun badgeNameOnly(): AdaptiveFragment {

    flowBox {
        gap { 8.dp }

        badge("suppressed")
        badge("success", theme = BadgeTheme.success)
        badge("info", theme = BadgeTheme.info)
        badge("warning", theme = BadgeTheme.warning)
        badge("error", theme = BadgeTheme.error)
        badge("important", theme = BadgeTheme.important)
    }

    return fragment()
}