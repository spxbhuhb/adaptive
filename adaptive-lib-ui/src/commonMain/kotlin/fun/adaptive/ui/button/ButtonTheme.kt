package `fun`.adaptive.ui.button

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.colors

class ButtonTheme(
    background : Color = colors.primary,
    foreground : Color = colors.onPrimary
) : AbstractTheme() {

    val container = instructionsOf(
        backgroundColor(background),
        inputCornerRadius,
        alignItems.center,
        height { inputHeightDp },
        paddingHorizontal { 20.dp }
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
        var DANGER = ButtonTheme(colors.danger, colors.onDanger)
    }

}