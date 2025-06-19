package `fun`.adaptive.ui.input.badge

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.theme.borders

open class BadgeInputTheme {

    var badgeTheme = BadgeTheme.info

    var container = instructionsOf(
        maxWidth,
        tabIndex { 0 }
    )

    var inputContainer = instructionsOf(
        maxWidth,
        fillStrategy.constrainReverse,
        paddingBottom { 8.dp }
    )

    var badgeContainer = instructionsOf(
        maxWidth,
        SizeStrategy(minHeight = badgeTheme.height + 16.dp),
        borders.outline,
        padding { 8.dp },
        cornerRadius { 4.dp },
        gap { 8.dp }
    )

    companion object {
        var default = BadgeInputTheme()
    }
}