package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.mpw.fragments.valueBadges
import `fun`.adaptive.value.AvValue

@Adaptive
fun valueBadgeExample(): AdaptiveFragment {

    // these registrations should be put into the `frontendAdapterInit`
    // function of the application module, they are here for the
    // clarity of the example only
    badgeThemeMap["online"] = BadgeTheme.success
    badgeThemeMap["alarm"] = BadgeTheme.error
    badgeThemeMap["configuration-pending"] = BadgeTheme.important

    val value = AvValue(
        name = "Example Value",
        statusOrNull = setOf("online", "alarm", "configuration-pending"),
        markersOrNull = setOf("zigbee", "controller", "thermostat"),
        spec = ""
    )

    examplePane(
        "valueBadges",
        """
            The `valueBadges` fragment can be used to displat the status and
            marker badges of an `AvValue` instance.
        """.trimIndent()
    ) {
        valueBadges(value)
    }

    return fragment()
}