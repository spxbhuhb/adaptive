package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.disabled as uiDisabled
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class InputTheme(
    val height: DPixel = 38.dp,
) {

    val base = instructionsOf(
        cornerRadius(8.dp),
        fontSize { 17.sp },
        lightFont,
        tabIndex { 0 }
    )

    val enabled = base + instructionsOf(
        border(colors.outline, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColors.onSurface,
        backgrounds.surface,
    )

    val focused = base + instructionsOf(
        border(colors.primary, 2.dp),
        padding(left = 15.dp, right = 15.dp),
        textColors.onSurface,
        backgrounds.surface,
    )

    val disabled = base + instructionsOf(
        uiDisabled,
        border(colors.outline, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColors.onSurface,
        backgrounds.surfaceVariant
    )

    val invalidFocused = base + instructionsOf(
        border(colors.fail, 2.dp),
        padding(left = 15.dp, right = 15.dp),
        textColors.onSurface,
        backgroundColor(colors.failSurface.opaque(0.05f)),
    )

    val invalidNotFocused = base + instructionsOf(
        border(colors.fail, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColors.onSurface,
        backgroundColor(colors.failSurface.opaque(0.05f)),
    )

    val singleLine = instructionsOf(
        height { height }
    )

    val textAreaFocused = instructionsOf(
        paddingTop { 5.dp },
        paddingLeft { 15.dp }
    )

    val textAreaNonFocused = instructionsOf(
        paddingTop { 6.dp },
        paddingLeft { 16.dp }
    )

    val unitContainer = instructionsOf(
        fill.constrainReverse
    )

    val unitText = instructionsOf(
        alignSelf.bottom,
        paddingLeft { 8.dp },
        paddingBottom { 5.dp },
    )

    companion object {
        var DEFAULT = InputTheme()
    }

}