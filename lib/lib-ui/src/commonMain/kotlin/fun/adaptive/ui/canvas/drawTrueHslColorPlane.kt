package `fun`.adaptive.ui.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import kotlin.math.abs
import kotlin.math.roundToInt

fun ActualCanvas.drawTrueHslColorPlane(
    baseHue: Double,
    width: Double,
    height: Double,
) {
    image(0.0, 0.0, width, height) { set ->

        val hPrime = baseHue / 60.0
        var i = 0
        var y = 0.0

        while (y < height) {
            var x = 0.0

            while (x < width) {
                val saturation = (x / width).coerceIn(0.0, 1.0)
                val lightness = (1.0 - (y / height)).coerceIn(0.0, 1.0)

                val c = (1.0 - abs(2.0 * lightness - 1.0)) * saturation
                val x1 = c * (1.0 - abs(hPrime % 2.0 - 1.0))

                var r1 = 0.0
                var g1 = 0.0
                var b1 = 0.0

                if (hPrime < 1.0) {
                    r1 = c
                    g1 = x1
                } else if (hPrime < 2.0) {
                    r1 = x1
                    g1 = c
                } else if (hPrime < 3.0) {
                    g1 = c
                    b1 = x1
                } else if (hPrime < 4.0) {
                    g1 = x1
                    b1 = c
                } else if (hPrime < 5.0) {
                    r1 = x1
                    b1 = c
                } else {
                    r1 = c
                    b1 = x1
                }

                val m = lightness - c / 2.0

                val r = ((r1 + m) * 255.0).roundToInt().coerceIn(0, 255)
                val g = ((g1 + m) * 255.0).roundToInt().coerceIn(0, 255)
                val b = ((b1 + m) * 255.0).roundToInt().coerceIn(0, 255)

                set(i ++, r)
                set(i ++, g)
                set(i ++, b)
                set(i ++, 255)

                x += 1.0
            }

            y += 1.0
        }
    }
}
