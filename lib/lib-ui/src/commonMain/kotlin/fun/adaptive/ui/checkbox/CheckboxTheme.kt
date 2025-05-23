package `fun`.adaptive.ui.checkbox

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textColors

class CheckboxTheme(
    containerSize: DPixel,
    contentSize: DPixel
) {

    var container = instructionsOf(
        size(containerSize),
        alignItems.center
    )

    var active = instructionsOf(
        size(contentSize, contentSize),
        cornerRadius(contentSize / 2.0),
        backgrounds.primary,
        textColors.onPrimary,
    )

    var inactive = instructionsOf(
        size(contentSize, contentSize),
        cornerRadius(contentSize / 2.0),
        border(colors.outline, 1.dp)
    )

    var icon = instructionsOf(
        noSelect,
        position(1.dp, 1.dp),
        svgHeight(contentSize - 2.0),
        svgWidth(contentSize - 2.0),
        iconColors.onPrimary
    )

    companion object {
        var DEFAULT = CheckboxTheme(38.dp, 20.dp)
        var small = CheckboxTheme(24.dp, 16.dp)
    }
}