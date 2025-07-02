package `fun`.adaptive.ui.input.time

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TimeInputTheme(
    val amPm : Boolean = false
): AbstractTheme() {

    val inputField = instructionsOf(
        width { if (amPm) 92.dp else 64.dp }
    )

    val dropdownPopup = instructionsOf(
        popupAlign.belowStart,
        marginTop(2.dp), // FIXME replace manual margin with popup config
        marginBottom(2.dp),
        zIndex { 200 },
        cornerRadius { 8.dp },
        border(colors.onSurface, 0.5.dp),
        backgrounds.surface,
        onClick { event -> event.stopPropagation() },
        dropShadow(colors.overlay.opaque(0.3), 8.dp, 8.dp, 8.dp)
    )

    val timePickerContainer = instructionsOf(
        size(312.dp, 380.dp)
    )

    val canvasSize = 312.dp
    val centerX = canvasSize.value / 2
    val centerY = canvasSize.value / 2

    val timePickerCanvas = instructionsOf(
        size(canvasSize)
    )

    val hourText = instructionsOf(
        fontSize { 24.sp },
        fontWeight { 400 }
    )

    val hours = calcHours()

    fun calcHours(): List<Triple<Double, Double, String>> {

        val radius = canvasSize.value / 3
        val radiansPerHour = 2 * PI / 12

        return (1 .. 12).map {
            val angle = - PI / 2 + (it * radiansPerHour)
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            Triple(x, y, it.toString())
        }
    }


    companion object {
        var default = TimeInputTheme()
    }
}