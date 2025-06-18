package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun commonBadgeExample(): AdaptiveFragment {

    // these registrations should be put into the `frontendAdapterInit`
    // function of the application module, they are here for the
    // clarity of the example only
    badgeThemeMap["success"] = BadgeTheme.success
    badgeThemeMap["info"] = BadgeTheme.info
    badgeThemeMap["warning"] = BadgeTheme.warning
    badgeThemeMap["error"] = BadgeTheme.error
    badgeThemeMap["important"] = BadgeTheme.important

    column {
        examplePane("name only") {
            flowBox {
                gap { 8.dp }

                badge("suppressed")
                badge("success", theme = BadgeTheme.success)
                badge("info", theme = BadgeTheme.info)
                badge("warning", theme = BadgeTheme.warning)
                badge("error", theme = BadgeTheme.error)
                badge("important", theme = BadgeTheme.important)
            }
        }

        examplePane("name, removable = true") {
            flowBox {
                gap { 8.dp }

                badge("suppressed", removable = true)
                badge("success", removable = true, theme = BadgeTheme.success)
                badge("info", removable = true, theme = BadgeTheme.info)
                badge("warning", removable = true, theme = BadgeTheme.warning)
                badge("error", removable = true, theme = BadgeTheme.error)
                badge("important", removable = true, theme = BadgeTheme.important)
            }
        }

        examplePane("name and icon") {
            flowBox {
                gap { 8.dp }

                badge("suppressed", Graphics.marker)
                badge("success", Graphics.success, theme = BadgeTheme.success)
                badge("info", Graphics.info, theme = BadgeTheme.info)
                badge("warning", Graphics.warning, theme = BadgeTheme.warning)
                badge("error", Graphics.error, theme = BadgeTheme.error)
                badge("important", Graphics.power_settings_new, theme = BadgeTheme.important)
            }
        }
        examplePane("name and icon, removable = true") {
            flowBox {
                gap { 8.dp }

                badge("suppressed", Graphics.marker, removable = true)
                badge("success", Graphics.success, removable = true, theme = BadgeTheme.success)
                badge("info", Graphics.info, removable = true, theme = BadgeTheme.info)
                badge("warning", Graphics.warning, removable = true, theme = BadgeTheme.warning)
                badge("error", Graphics.error, removable = true, theme = BadgeTheme.error)
                badge("important", Graphics.power_settings_new, removable = true, theme = BadgeTheme.important)
            }
        }

        examplePane("name only, useSeverity = true") {
            flowBox {
                gap { 8.dp }

                badge("suppressed", useSeverity = true)
                badge("success", useSeverity = true)
                badge("info", useSeverity = true)
                badge("warning", useSeverity = true)
                badge("error", useSeverity = true)
                badge("important", useSeverity = true)
            }
        }
    }

    return fragment()
}