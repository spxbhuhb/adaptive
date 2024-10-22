package `fun`.adaptive.ui.checkbox.api.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textColors

class CheckboxTheme {

    var active = instructionsOf(
        size(20.dp, 20.dp),
        cornerRadius(10.dp),
        backgrounds.primary,
        textColors.onPrimary,
    )

    var inactive = instructionsOf(
        size(20.dp, 20.dp),
        cornerRadius(10.dp),
        border(colors.outline, 1.dp)
    )

    var icon = instructionsOf(
        noSelect,
        position(1.dp, 1.dp),
        svgHeight(18.dp),
        svgWidth(18.dp),
        iconColors.onPrimary
    )

}