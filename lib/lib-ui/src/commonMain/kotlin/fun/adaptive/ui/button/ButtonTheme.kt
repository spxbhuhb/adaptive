package `fun`.adaptive.ui.button

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

class ButtonTheme(
    background : Color = colors.primary,
    foreground : Color = colors.onPrimary,
    border : Border = border(colors.outline, 0.dp),
) : AbstractTheme() {

    val container = instructionsOf(
        gap { 6.dp },
        backgroundColor(background),
        inputCornerRadius,
        alignItems.center,
        height { inputHeightDp },
        paddingHorizontal { 16.dp },
        border
    )

    val text = instructionsOf(
        textColor(foreground),
        buttonFont,
        noSelect,
        paddingTop(2.dp)
    )

    val icon = instructionsOf(
        size(18.dp, 18.dp),
        fill(foreground),
        svgWidth(18.dp),
        svgHeight(18.dp),
        noSelect
    )

    companion object {
        var DEFAULT = ButtonTheme()
        var noFocus = ButtonTheme(colors.surface, colors.onSurface, borders.outline)
        var DANGER = ButtonTheme(colors.danger, colors.onDanger)
    }

}