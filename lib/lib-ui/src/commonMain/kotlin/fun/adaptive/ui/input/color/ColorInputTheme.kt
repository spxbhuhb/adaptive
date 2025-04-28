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

    var inputContainer = instructionsOf(
        gap { 8.dp },
        fill.constrain,
        alignItems.center
    )

    fun inputExample(color: Color) = instructionsOf(
        size(24.dp),
        border(if (color.brightness() > 128.0) colors.overlay else colors.reverse),
        backgroundColor { color },
        cornerRadius { 4.dp }
    )

    var pickerContainer = instructionsOf(
        colTemplate(exampleSize, 1.fr),
        rowTemplate(exampleSize, 38.dp, 38.dp)
    )

    var pickerExample = instructionsOf(
        size(exampleSize),
        cornerTopLeftRadius(4.dp),
        cornerBottomLeftRadius(4.dp)
    )

    var colorPlane = instructionsOf(
        maxWidth,
        height(exampleSize),
        cornerTopRightRadius(4.dp),
        cornerBottomRightRadius(4.dp)
    )

    var hueSliderContainer = instructionsOf(
        maxWidth,
        colSpan(2),
        padding(16.dp, 24.dp, 16.dp, 24.dp)
    )

    var hueSlider = instructionsOf(
        maxWidth,
        height(8.dp),
        cornerRadius { 4.dp }
    )

    companion object {
        var default = ColorInputTheme()
    }
}