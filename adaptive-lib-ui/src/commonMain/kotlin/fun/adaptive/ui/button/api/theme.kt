package `fun`.adaptive.ui.button.api

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

var buttonTheme = ButtonTheme()
var dangerButtonTheme = ButtonTheme(colors.danger, colors.onDanger)

class ButtonTheme(
    background : Color = colors.primary,
    foreground : Color = colors.onPrimary
) {
    val container = instructionsOf(
        backgroundColor(background),
        cornerRadius(10.dp),
        alignItems.center,
        gap(6.dp),
        size(153.dp, 38.dp),
        paddingRight(10.dp)
    )

    val text = instructionsOf(
        textColor(foreground),
        fontSize(14.sp),
        lightFont,
        noSelect
    )

    val icon = instructionsOf(
        size(22.dp, 22.dp),
        fill(foreground),
        svgWidth(22.dp),
        svgHeight(22.dp),
        noSelect
    )

}