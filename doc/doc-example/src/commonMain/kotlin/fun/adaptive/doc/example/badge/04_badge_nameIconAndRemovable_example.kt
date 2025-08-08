package `fun`.adaptive.doc.example.badge

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
 * # With name, icon and removable
 *
 * Pass a graphics resource in `icon`, use `removable = true` **AND** pass `removeFun`:
 *
 * - the badge will show the icon on the left,
 * - the badge will have a close icon on the right.
 */
@Adaptive
fun badgeNameIconAndRemovable(): AdaptiveFragment {

    flowBox {
        gap { 8.dp }

        badge("suppressed", Graphics.marker, removable = true) { }
        badge("success", Graphics.success, removable = true, theme = BadgeTheme.success) { }
        badge("info", Graphics.info, removable = true, theme = BadgeTheme.info) { }
        badge("warning", Graphics.warning, removable = true, theme = BadgeTheme.warning) { }
        badge("error", Graphics.error, removable = true, theme = BadgeTheme.error) { }
        badge("important", Graphics.power_settings_new, removable = true, theme = BadgeTheme.important) { }
    }

    return fragment()
}