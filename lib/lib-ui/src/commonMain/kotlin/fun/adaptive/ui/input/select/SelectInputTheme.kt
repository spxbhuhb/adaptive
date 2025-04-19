package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.decoration.BackgroundColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

class SelectInputTheme {

    var listContainer = instructionsOf(
        tabIndex { 0 }
    )

    var surfaceListContainerBase = instructionsOf(
        listContainer,
        padding(6.dp, 8.dp, 6.dp, 8.dp)
    )

    var surfaceListContainerBaseFocused = instructionsOf(
        listContainer,
        padding(5.dp, 7.dp, 5.dp, 7.dp)
    )

    var optionContainerBase = instructionsOf(
        maxWidth,
        height { 26.dp },
        alignItems.startCenter,
        padding(4.dp, 8.dp, 4.dp, 8.dp),
        gap { 8.dp }
    )

    val optionContainerSelected = instructionsOf(
        optionContainerBase,
        cornerRadius { 4.dp },
        BackgroundColor(colors.selectedSurfaceFocus)
    )

    var optionIcon = instructionsOf(
        size(18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    var optionText = instructionsOf(
        fontSize(13.sp),
        noSelect
    )

    companion object {
        val default = SelectInputTheme()
    }

}