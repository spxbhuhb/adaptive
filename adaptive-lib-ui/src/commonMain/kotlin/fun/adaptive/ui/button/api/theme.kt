package `fun`.adaptive.ui.button.api

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textColors

var buttonTheme = ButtonTheme()

class ButtonTheme {

    val container = instructionsOf(
        backgrounds.primary,
        cornerRadius(10.dp),
        alignItems.center,
        gap(6.dp),
        size(153.dp, 38.dp),
        paddingRight(10.dp)
    )

    val text = instructionsOf(
        textColors.onPrimary,
        fontSize(14.sp),
        lightFont,
        noSelect
    )

    val icon = instructionsOf(
        size(22.dp, 22.dp),
        iconColors.onPrimary,
        svgWidth(22.dp),
        svgHeight(22.dp),
        noSelect
    )

}
