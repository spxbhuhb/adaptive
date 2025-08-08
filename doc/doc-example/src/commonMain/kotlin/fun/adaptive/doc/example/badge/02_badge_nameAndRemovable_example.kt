package `fun`.adaptive.doc.example.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.instruction.dp

/**
 * # With name and removable
 *
 * Use `removable = true` **AND** pass `removeFun`:
 *
 * - the badge will have a close icon.
 */
@Adaptive
fun badgeNameAndRemovable(): AdaptiveFragment {

    flowBox {
        gap { 8.dp }

        badge("suppressed", removable = true) { }
        badge("success", removable = true, theme = BadgeTheme.success) { }
        badge("info", removable = true, theme = BadgeTheme.info) { }
        badge("warning", removable = true, theme = BadgeTheme.warning) { }
        badge("error", removable = true, theme = BadgeTheme.error) { }
        badge("important", removable = true, theme = BadgeTheme.important) { }
    }

    return fragment()
}