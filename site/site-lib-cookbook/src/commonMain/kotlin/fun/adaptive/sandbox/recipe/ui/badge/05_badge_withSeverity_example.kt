package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.instruction.dp

/**
 * # With severity
 *
 * With `useSeverity = true`, the fragment looks up the `name` of the
 * badge in `badgeThemeMap` and uses the corresponding theme.
 *
 * This is very convenient when displaying status badges which may
 * have different severities.
 */
@Adaptive
fun badgeWithSeverity(): AdaptiveFragment {

    // these registrations should be put into the `frontendAdapterInit`
    // function of the application module, they are here for the
    // clarity of the example only
    badgeThemeMap["success"] = BadgeTheme.success
    badgeThemeMap["info"] = BadgeTheme.info
    badgeThemeMap["warning"] = BadgeTheme.warning
    badgeThemeMap["error"] = BadgeTheme.error
    badgeThemeMap["important"] = BadgeTheme.important

    flowBox {
        gap { 8.dp }

        badge("suppressed", useSeverity = true)
        badge("success", useSeverity = true)
        badge("info", useSeverity = true)
        badge("warning", useSeverity = true)
        badge("error", useSeverity = true)
        badge("important", useSeverity = true)
    }

    return fragment()
}