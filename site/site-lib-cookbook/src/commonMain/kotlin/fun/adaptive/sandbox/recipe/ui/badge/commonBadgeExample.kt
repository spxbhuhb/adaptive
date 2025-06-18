package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.error
import `fun`.adaptive.ui.generated.resources.marker
import `fun`.adaptive.ui.generated.resources.power_settings_new
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun commonBadgeExample() : AdaptiveFragment {

    // these registrations should be put into the `frontendAdapterInit`
    // function of the application module, they are here for the
    // clarity of the example only
    badgeThemeMap["Example Success Badge"] = BadgeTheme.success
    badgeThemeMap["Example Info Badge"] = BadgeTheme.info
    badgeThemeMap["Example Warning Badge"] = BadgeTheme.warning
    badgeThemeMap["Example Error Badge"] = BadgeTheme.error
    badgeThemeMap["Example Important Badge"] = BadgeTheme.important

    examplePane("Variations of the `badge` fragment") {
        grid {
            colTemplate(200.dp, 1.fr)
            rowTemplate(extend = 30.dp)

            badge("Example Badge")
            text("name only, default theme")

            badge("Example Badge", theme = BadgeTheme.error)
            text("name only, error theme (manually set)")

            badge("Example Badge", Graphics.marker)
            text("name and an icon, default theme")

            badge("Example Badge", Graphics.error, theme = BadgeTheme.error)
            text("name and an icon, error theme  (manually set)")

            badge("Example Success Badge", useSeverity = true)
            text("name only, icon and theme automatically by severity")

            badge("Example Info Badge", useSeverity = true)
            text("name only, icon and theme automatically by severity")

            badge("Example Warning Badge", useSeverity = true)
            text("name only, icon and theme automatically by severity")

            badge("Example Error Badge", useSeverity = true)
            text("name only, icon and theme automatically by severity")

            badge("Example Important Badge", Graphics.power_settings_new, useSeverity = true)
            text("name and icon, theme automatically by severity")

        }
    }

    return fragment()
}