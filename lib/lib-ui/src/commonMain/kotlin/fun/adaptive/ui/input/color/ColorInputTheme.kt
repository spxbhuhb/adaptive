package `fun`.adaptive.ui.input.color

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors

class ColorInputTheme {

    val exampleSize = 160.dp
    val colorPaneWidth = 360.dp

    var inputContainer = instructionsOf(
        gap { 8.dp },
        fillStrategy.constrain,
        alignItems.startCenter
    )

    fun inputExample(color: Color) = instructionsOf(
        size(24.dp),
        border(if (color.brightness() > 128.0) colors.overlay else colors.reverse),
        backgroundColor { color },
        cornerRadius { 4.dp }
    )

    var pickerContainer = instructionsOf(

    )

    var pickerExample = instructionsOf(
        size(exampleSize),
        cornerTopLeftRadius(4.dp),
        cornerBottomLeftRadius(4.dp)
    )

    //colTemplate(exampleSize, 1.fr),
    //        rowTemplate(exampleSize, 38.dp, 38.dp)
    var colorPlane = instructionsOf(
        width(colorPaneWidth),
        height(exampleSize),
        cornerTopRightRadius(4.dp),
        cornerBottomRightRadius(4.dp)
    )

    val sliderHorizontalPadding = 24

    var hueSliderOuterContainer = instructionsOf(
        width(exampleSize + colorPaneWidth),
        height(38.dp)
    )

    var hueSliderInnerContainer = instructionsOf(
        maxWidth,
        padding(16.dp, sliderHorizontalPadding.dp, 16.dp, sliderHorizontalPadding.dp)
    )

    var hueSlider = instructionsOf(
        tabIndex { 0 },
        maxWidth,
        height(8.dp),
        cornerRadius { 4.dp }
    )

    companion object {
        var default = ColorInputTheme()
    }
}