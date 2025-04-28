package `fun`.adaptive.ui.input.color

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.draw
import `fun`.adaptive.graphics.canvas.fragment.ActualCanvasOwner
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.canvas.drawTrueHslColorPlane
import `fun`.adaptive.ui.canvas.drawHueSlider
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modalForEdit
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun colorPickerPopup(
    viewBackend: ColorInputViewBackend,
    hide: () -> Unit
) {

    val pickerViewBackend = valueFrom { ColorPickerViewBackend(viewBackend.inputValue) }

    modalForEdit(
        title = viewBackend.label ?: "",
        hide = hide,
        save = {
            viewBackend.inputValue = pickerViewBackend.inputValue
            hide()
        },
        theme = viewBackend.popupTheme
    ) {
        column {
            padding { 16.dp }
            colorPicker(pickerViewBackend)
        }
    }
}

@Adaptive
fun colorPicker(
    viewBackend: ColorPickerViewBackend
) {

    val theme = viewBackend.colorInputTheme

    val hexBackend = textInputBackend(viewBackend.safeHex()) {
        this.label = "Hex"
        onChange = viewBackend::fromString
    }

    grid(theme.pickerContainer) {
        size(500.dp, 400.dp)
        box {
            theme.pickerExample
            backgroundColor { viewBackend.inputValue ?: colors.transparent }
        }

        colorPlaneContainer(viewBackend)

        hueSlider(viewBackend)

        textInput2(hexBackend)
    }
}

@Adaptive
fun colorPlaneContainer(
    viewBackend: ColorPickerViewBackend
) {
    val hue = (viewBackend.inputValue ?: colors.transparent).encodeToHsl().hue
    val self = fragment()

    box {
        viewBackend.colorInputTheme.colorPlane

        onClick { event ->
            val actualCanvas = self.first<ActualCanvasOwner>().canvas

            val saturation = (event.x / actualCanvas.width).coerceIn(0.0, 1.0)
            val lightness = (1.0 - (event.y / actualCanvas.height)).coerceIn(0.0, 1.0)

            viewBackend.inputValue = Color.decodeFromHsl(hue = hue, saturation = saturation, lightness = lightness)
        }

        canvas {
            draw {
                drawTrueHslColorPlane(hue, width = width * 2, height = height * 2)
            }
        }
    }
}

@Adaptive
fun hueSlider(
    viewBackend: ColorPickerViewBackend
) {
    val theme = viewBackend.colorInputTheme

    box {
        theme.hueSliderContainer
        canvas {
            theme.hueSlider
            draw { drawHueSlider(width = width, height = height, top = 0.0) }
        }
    }
}
