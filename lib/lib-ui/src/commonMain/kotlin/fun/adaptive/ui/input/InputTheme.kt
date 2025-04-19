package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.input.Disabled
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class InputTheme : AbstractTheme() {

    val paddingB1 = padding(top = 1.dp, left = 12.dp, bottom = 1.dp, right = 12.dp)
    val paddingB2 = padding(left = 11.dp, right = 11.dp)

    val base = instructionsOf(
        inputCornerRadius,
        inputFont,
        tabIndex { 0 }
    )

    val enabled = base + instructionsOf(
        border(colors.outline, 1.dp),
        paddingB1,
        textColors.onSurface,
        backgrounds.surface,
    )

    val focused = base + instructionsOf(
        border(colors.primary, 2.dp),
        paddingB2,
        textColors.onSurface,
        backgrounds.surface,
    )

    val disabled = base + instructionsOf(
        Disabled(),
        border(colors.outline, 1.dp),
        paddingB1,
        textColors.onSurface,
        backgrounds.surfaceVariant
    )

    val invalidFocused = base + instructionsOf(
        border(colors.fail, 2.dp),
        paddingB2,
        textColors.onSurface,
        backgrounds.surface
        //backgroundColor(colors.failSurface.opaque(0.05f))
    )

    val invalidNotFocused = base + instructionsOf(
        border(colors.fail, 1.dp),
        paddingB1,
        textColors.onSurface,
        backgrounds.surface
        //backgroundColor(colors.failSurface.opaque(0.05f)),
    )

    val singleLine = instructionsOf(
        height { inputHeightDp }
    )

    val textAreaFocused = instructionsOf(
        paddingTop { 5.dp },
        paddingLeft { 6.dp }
    )

    val textAreaNonFocused = instructionsOf(
        paddingTop { 6.dp },
        paddingLeft { 7.dp }
    )

    val unitContainer = instructionsOf(
        fill.constrainReverse
    )

    val unitText = instructionsOf(
        alignSelf.bottom,
        paddingLeft { 8.dp }
    )

    companion object {
        var DEFAULT = InputTheme()
    }

}