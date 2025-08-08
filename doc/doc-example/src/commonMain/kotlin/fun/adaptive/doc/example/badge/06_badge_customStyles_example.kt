package `fun`.adaptive.doc.example.badge

import `fun`.adaptive.doc.example.generated.resources.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

/**
 * # With custom styles
 *
 * - Customize the appearance of badges by defining a `BadgeTheme`.
 * - `BadgeTheme` offers a number of customization options for colors, sizing, etc.
 */
@Adaptive
fun badgeCustomStyles(): AdaptiveFragment {

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