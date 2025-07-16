package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.BackgroundColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class SelectInputTheme(
    val optionHeight: DPixel = 26.dp
) : AbstractTheme() {

    // List

    var listContainer = instructionsOf(
        tabIndex { inputTabIndex },
        verticalScroll,
        fillStrategy.resizeToMax
    )

    var surfaceListContainerBase = instructionsOf(
        listContainer,
        padding(6.dp, 8.dp, 6.dp, 8.dp)
    )

    var surfaceListContainerBaseFocused = instructionsOf(
        listContainer,
        padding(5.dp, 7.dp, 5.dp, 7.dp)
    )

    var surfaceListContainerDisabled = instructionsOf(
        padding(6.dp, 8.dp, 6.dp, 8.dp),
        verticalScroll
    )

    // Dropdown

    val dropdownPopup = instructionsOf(
        marginTop(2.dp), // FIXME replace manual margin with popup config
        marginBottom(2.dp),
        height { inputHeightDp * 10 },
        verticalScroll,
        zIndex { 200 },
        cornerRadius { 8.dp },
        border(colors.onSurface, 0.5.dp),
        backgrounds.surface
    )

    val dropdownOptionsContainer = instructionsOf(
        focusFirst,
        tabIndex { 0 },
        fillStrategy.resizeToMax,
        alignItems.startCenter,
        padding(6.dp, 8.dp, 6.dp, 8.dp)
    )

    // Options

    var optionContainerBase = instructionsOf(
        maxWidth,
        height { optionHeight },
        alignItems.startCenter,
        padding(4.dp, 8.dp, 4.dp, 8.dp),
        gap { 8.dp }
    )

    val optionContainerSelected = instructionsOf(
        optionContainerBase,
        cornerRadius { 4.dp },
        backgrounds.selectedFocusedSurface
    )

    val optionContainerHover = instructionsOf(
        optionContainerBase,
        cornerRadius { 4.dp },
        BackgroundColor(colors.hoverSurface)
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

    // Value

    var valueContainer = instructionsOf(
        maxWidth,
        height { 26.dp },
        alignItems.startCenter,
        padding(4.dp, 0.dp, 4.dp, 0.dp),
        gap { 8.dp },
        colTemplate(1.fr, 24.dp)
    )

    var valueIcon = instructionsOf(
        size(18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    var valueText = instructionsOf(
        fontSize(13.sp),
        noSelect,
        normalFont
    )

    var noValue = instructionsOf(
        fontSize(13.sp),
        noSelect,
        textColors.onSurfaceVariant
    )

    var dropdownIcon = instructionsOf(
        fill(colors.onSurfaceVariant),
        size(18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    companion object {
        val default = SelectInputTheme()
    }

}