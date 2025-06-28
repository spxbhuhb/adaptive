package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.mpw.fragments.contentPaneHeader
import `fun`.adaptive.value.AvValue

/**
 * # contentPaneHeader
 *
 * The `contentPaneHeader` fragment can be used to display a header for a
 * content pane, typically to show an `AvValue` instance.
 *
 * You can specify a renderer for the actions which are placed on the
 * right side of the header.
 *
 * Badges are automatically added under the UUID, statuses first, markers after.
 *
 * `contentPaneHeader` sets `useSeverity` to `true` to style the badges,
 * add them to `badgeThemeMap` in the `frontendAdapterInit` function of the
 * module.
 */
@Adaptive
fun badgeContentPaneHeader(): AdaptiveFragment {

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

    contentPaneHeader("Example Content Pane Title", value.uuid, value) {
        submitButton("Example Button") { }
    }

    return fragment()
}