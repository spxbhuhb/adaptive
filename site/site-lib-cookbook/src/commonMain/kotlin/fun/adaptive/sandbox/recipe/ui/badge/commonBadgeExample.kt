package `fun`.adaptive.sandbox.recipe.ui.badge

import `fun`.adaptive.cookbook.generated.resources.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

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
        gap { 24.dp }

        examplePane(
            "Only with name",
            """
                * Each badge has a `name`, this is the only mandatory parameter.
                * The colors are defined by the theme, which you can pass in `theme`.
                * Suppressed badge theme meant to display secondary information.
            """.trimIndent()
        ) {
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

        examplePane(
            "With name and removable",
            """
                When you use `removable = true` **AND** pass`removeFun`, the badge will have a close icon.
            """.trimIndent()
        ) {
            flowBox {
                gap { 8.dp }

                badge("suppressed", removable = true) {  }
                badge("success", removable = true, theme = BadgeTheme.success) {  }
                badge("info", removable = true, theme = BadgeTheme.info) {  }
                badge("warning", removable = true, theme = BadgeTheme.warning) {  }
                badge("error", removable = true, theme = BadgeTheme.error) {  }
                badge("important", removable = true, theme = BadgeTheme.important) {  }
            }
        }

        examplePane(
            "With name and icon",
            """
                You can pass an icon to show in the `icon` parameter.
            """.trimIndent()
        ) {
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

        examplePane(
            "With name, icon and removable",
            """
                When you use `removable = true` **AND** pass`removeFun`, the badge will have a close icon.
            """.trimIndent()
        ) {
            flowBox {
                gap { 8.dp }

                badge("suppressed", Graphics.marker, removable = true) {  }
                badge("success", Graphics.success, removable = true, theme = BadgeTheme.success) {  }
                badge("info", Graphics.info, removable = true, theme = BadgeTheme.info) {  }
                badge("warning", Graphics.warning, removable = true, theme = BadgeTheme.warning) {  }
                badge("error", Graphics.error, removable = true, theme = BadgeTheme.error) {  }
                badge("important", Graphics.power_settings_new, removable = true, theme = BadgeTheme.important) {  }
            }
        }

        examplePane(
            "With name and severity",
            """
                With `useSeverity = true`, the fragment looks up the name of the
                badge in `badgeThemeMap` and uses the corresponding theme.
                
                This is very convenient when displaying status badges which may have different
                severities.
            """.trimIndent()
        ) {
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

        badgeStyles()
    }

    return fragment()
}

private val roundedOrange = BadgeTheme(
    border = color(0xfcba03),
    iconColor = colors.onSurface,
    iconColorHover = colors.onSurface,
    textColor = colors.onSurface,
    iconResource = Graphics.info,
    cornerRadius = 10.dp
)

private val counter = BadgeTheme(
    height = 18.dp,
    border = colors.fail,
    background = colors.fail,
    iconColor = colors.onFailSurface,
    iconColorHover = colors.onSurface,
    textColor = colors.white,
    textInstructions = instructionsOf(semiBoldFont, fontSize { 12.sp }),
    iconResource = Graphics.info,
    cornerRadius = 10.dp
)

@Adaptive
private fun badgeStyles() {

    examplePane(
        "With custom styles",
        """
            * You can customize the appearance of badges by defining a `BadgeTheme`.
            * `BadgeTheme` offers a number of customisation options for colors, sizing etc.
        """.trimIndent()
    ) {
        flowBox {
            gap { 8.dp }

            badge("orange", theme = roundedOrange)

            // badge height = 18.dp, icon height = 24.dp
            // width should accommodate badge size with large numbers
            box {
                height(33.dp) .. width { 72.dp }
                icon(Graphics.mail) .. position(9.dp, 0.dp)
                badge("6", theme = counter) .. position(0.dp, 16.dp)
            }
        }
    }
}