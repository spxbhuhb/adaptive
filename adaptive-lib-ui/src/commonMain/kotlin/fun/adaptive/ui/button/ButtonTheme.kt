package `fun`.adaptive.ui.button

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

class ButtonTheme(
    background : Color = colors.primary,
    foreground : Color = colors.onPrimary,
    border : Border = border(colors.outline, 0.dp),
) : AbstractTheme() {

    val container = instructionsOf(
        backgroundColor(background),
        inputCornerRadius,
        alignItems.center,
        height { inputHeightDp },
        paddingHorizontal { 20.dp },
        border
    )

    val text = instructionsOf(
        textColor(foreground),
        buttonFont,
        noSelect,
        paddingTop(2.dp)
    )

    val icon = instructionsOf(
        size(22.dp, 22.dp),
        fill(foreground),
        svgWidth(22.dp),
        svgHeight(22.dp),
        noSelect
    )

    companion object {
        var DEFAULT = ButtonTheme()
        var noFocus = ButtonTheme(colors.surface, colors.onSurface, borders.outline)
        var DANGER = ButtonTheme(colors.danger, colors.onDanger)
    }

}