package `fun`.adaptive.ui.input.time

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.circle
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.utility.localTime
import kotlinx.datetime.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Adaptive
fun timePicker(
    value: LocalTime = localTime(),
    close: () -> Unit,
    onChange: (LocalTime) -> Unit,
    theme: TimeInputTheme = TimeInputTheme.default
) {
    @Independent
    val initValue = value
    val size = theme.canvasSize.value // FIXME dp/px ratio from adapter

    box {
        theme.timePickerContainer

        canvas {
            theme.timePickerCanvas

            for (hour in theme.hours) {
                fillText(hour.first, hour.second, hour.third, PopupAlign.centerCenter) .. theme.hourText
            }

            circle(theme.centerX, theme.centerY, 4.0)
        }
    }
}