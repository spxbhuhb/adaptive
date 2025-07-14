package `fun`.adaptive.ui.input.color

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.circle
import `fun`.adaptive.graphics.canvas.api.draw
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.fragment.ActualCanvasOwner
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.canvas.drawFakeHslColorPlane
import `fun`.adaptive.ui.canvas.drawHueSlider
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.decoration.HslColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.popup.modal.editorModal
import `fun`.adaptive.ui.theme.colors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Adaptive
fun colorPickerPopup(
    viewBackend: ColorInputViewBackend,
    hide: () -> Unit
) {

    val pickerViewBackend = observe { HslColorPickerViewBackend(viewBackend.inputValue?.encodeToHsl()) }

    editorModal(
        title = viewBackend.label ?: "",
        hide = hide,
        save = {
            viewBackend.inputValue = pickerViewBackend.inputValue?.toColor()
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
    viewBackend: HslColorPickerViewBackend
) {

    val theme = viewBackend.colorInputTheme

    val hexBackend = textInputBackend(viewBackend.safeHex()) {
        this.label = "Hex"
        onChange = viewBackend::fromString
    }

    column(theme.pickerContainer) {
        row {
            box {
                theme.pickerExample
                backgroundColor { viewBackend.inputValue?.toColor() ?: colors.transparent }
            }

            colorPlaneContainer(viewBackend)
        }

        hueSlider(viewBackend)

        textInput(hexBackend) .. width { 120.dp }
    }
}

@Adaptive
fun colorPlaneContainer(
    viewBackend: HslColorPickerViewBackend
) {
    val hue = viewBackend.inputValue?.hue ?: 0.0
    val self = fragment()

    box {
        viewBackend.colorInputTheme.colorPlane

        onClick { event ->
            val actualCanvas = self.first<ActualCanvasOwner>().canvas

            val fx = event.x / actualCanvas.width
            val fy = event.y / actualCanvas.height

            // calculate HSV first
            val saturation = fx.coerceIn(0.0, 1.0)
            val value = (1 - fy).coerceIn(0.0, 1.0)

            // convert HSV to HSL TODO use HSL to RGB directly
            val lightness = value * (1 - saturation / 2).coerceIn(0.0, 1.0)
            viewBackend.inputValue = HslColor(
                hue,
                if (abs(lightness) < 0.000001 || abs(lightness - 1.0) < 0.000001) {
                    0.0
                } else {
                    (value - lightness) / min(lightness, 1 - lightness)
                },
                lightness,
                1.0
            )
        }

        canvas {
            maxSize
            draw {
                // TODO think about fake vs. true HSL color pane
                drawFakeHslColorPlane(hue, width = width, height = height)
            } .. name("$hue") // FIXME using name as patch bug (dependency related) workaround
        }
    }
}

@Adaptive
fun hueSlider(
    viewBackend: HslColorPickerViewBackend
) {
    val theme = viewBackend.colorInputTheme
    val hslColor = viewBackend.inputValue !!

    box {
        theme.hueSliderOuterContainer

        box {
            theme.hueSliderInnerContainer

            canvas { size ->
                theme.hueSlider


                onClick { event ->
                    updateBackend(viewBackend, 360.0 * event.x / event.fragment.renderData.finalWidth)
                }

                onKeyDown { event ->
                    when (event.keyInfo?.key) {
                        Keys.ARROW_LEFT -> updateBackend(viewBackend, null, - 1.0)
                        Keys.ARROW_RIGHT -> updateBackend(viewBackend, null, 1.0)
                    }
                }

                draw {
                    drawHueSlider(width = width, height = height, top = 0.0)
                }
            }
        }

        // FIXME replace this with a box and make it moveable
        canvas { size ->
            maxSize .. noPointerEvents

            circle(
                handlePosition(theme, size, hslColor),
                size.height / 2.0 + 1.0,
                9.0
            ) .. fill(colors.white)

            circle(
                handlePosition(theme, size, hslColor),
                size.height / 2.0 + 1.0,
                8.0
            ) .. fill(HslColor(hslColor.hue, 1.0, 0.5, 1.0).toColor())
        }
    }
}

private fun handlePosition(
    theme: ColorInputTheme,
    size: RawSize,
    hslColor: HslColor
) =
    theme.sliderHorizontalPadding + (size.width - 2 * theme.sliderHorizontalPadding) * hslColor.hue / 360.0

private fun updateBackend(viewBackend: HslColorPickerViewBackend, hue: Double? = null, adjustment: Double = 0.0) {
    val currentHsl = viewBackend.inputValue !!

    viewBackend.inputValue = HslColor(
        hue = hue ?: min(360.0, max(0.0, currentHsl.hue + adjustment)),
        saturation = currentHsl.saturation,
        lightness = currentHsl.lightness,
        opacity = currentHsl.opacity
    )
}