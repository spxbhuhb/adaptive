/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.*
import `fun`.adaptive.graphics.canvas.model.gradient.Gradient
import `fun`.adaptive.graphics.canvas.model.gradient.LinearGradient
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.AuiText.Companion.measureContext
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Uint8ClampedArray
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ImageData
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.tan

class ActualBrowserCanvas : ActualCanvas {

    val receiver = newElement()
    val receiverContext = newContext(receiver)

    val buffer = newElement()
    val bufferContext = newContext(buffer)

    var saveIds = mutableListOf<Long>()

    override var shouldRedraw: Boolean = true

    override val width
        get() = currentWidth

    override val height: Double
        get() = currentHeight

    var currentWidth = 0.0

    var currentHeight = 0.0

    override fun redrawNeeded() {
        shouldRedraw = true
    }

    fun newElement() =
        (document.createElement("canvas") as HTMLCanvasElement).also {
            it.style.imageRendering = "crisp-edges"
        }

    fun newContext(element: HTMLCanvasElement) =
        element.getContext("2d") as CanvasRenderingContext2D

    override fun setSize(width: Double, height: Double) {
        if (width == currentWidth && height == currentHeight) return

        currentWidth = width
        currentHeight = height

        val scale = window.devicePixelRatio

        val scaledWidth = (width * scale).toInt()
        val scaledHeight = (height * scale).toInt()

        receiver.width = scaledWidth
        receiver.height = scaledHeight

        buffer.width = scaledWidth
        buffer.height = scaledHeight

        bufferContext.scale(scale, scale)
    }


    override fun save(id: Long) {
        if (saveIds.lastOrNull() == id) return
        saveIds.add(id)
        bufferContext.save()
    }

    override fun restore(id: Long) {
        if (saveIds.lastOrNull() != id) return
        saveIds.removeLastOrNull()
        bufferContext.restore()
    }

    override fun draw(drawFun: () -> Unit) {
        window.requestAnimationFrame {
            clear()
            drawFun()

            receiverContext.clearRect(0.0, 0.0, receiver.width.toDouble(), receiver.height.toDouble())
            receiverContext.drawImage(buffer, 0.0, 0.0)
        }
    }

    override fun arc(
        cx: Double,
        cy: Double,
        radius: Double,
        startAngle: Double,
        endAngle: Double,
        anticlockwise: Boolean
    ) {
        bufferContext.beginPath()
        bufferContext.arc(cx, cy, radius, startAngle, endAngle, anticlockwise)
        bufferContext.fill()
    }

    override fun newPath(): ActualBrowserPath {
        return ActualBrowserPath()
    }

    override fun fill(path: ActualPath) {
        path as ActualBrowserPath
        bufferContext.fill(path.receiver)
    }

    override fun stroke(path: ActualPath) {
        path as ActualBrowserPath
        bufferContext.stroke(path.receiver)
    }

    override fun fill() {
        bufferContext.fill()
    }

    override fun fillText(x: Double, y: Double, text: String) {
        bufferContext.fillText(text, x, y)
    }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        bufferContext.fillRect(x, y, width, height)
    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        bufferContext.beginPath()
        bufferContext.moveTo(x1, y1)
        bufferContext.lineTo(x2, y2)
        bufferContext.stroke()
    }

    // Kotlin/JS version of set(index:Int,value:Byte) does not work for values above 127
    fun Uint8ClampedArray.set(index: Int, value: Int) {
        asDynamic()[index] = value
    }

    override fun image(x: Double, y: Double, width: Double, height: Double, drawFun: ((Int, Int) -> Unit) -> Unit) {
        val imageData = bufferContext.createImageData(ImageData(width.toInt(), height.toInt()))
        val data = imageData.data

        drawFun { index, value -> data.set(index, value) }

        bufferContext.putImageData(imageData, x, y)
    }

    override fun transform(t: CanvasTransformInstruction) {
        when (t) {
            is Translate -> bufferContext.translate(t.tx, t.ty)

            is Rotate -> {
                bufferContext.translate(- t.cx, - t.cy)
                bufferContext.rotate(t.rotateAngle)
                bufferContext.translate(t.cx, t.cy)
            }

            is Scale -> bufferContext.scale(t.sx, t.sy)

            is Matrix -> bufferContext.transform(t.a, t.b, t.c, t.d, t.e, t.f)

            is SkewX -> {
                val rad = t.skewAngle * PI / 180
                bufferContext.transform(1.0, 0.0, tan(rad), 1.0, 0.0, 0.0)
            }

            is SkewY -> {
                val rad = t.skewAngle * PI / 180
                bufferContext.transform(1.0, tan(rad), 0.0, 1.0, 0.0, 0.0)
            }
        }
    }

    override fun setFont(font: String) {
        bufferContext.font = font
    }

    override fun setStroke(color: Color) {
        bufferContext.strokeStyle = color.hex
    }

    override fun setFill(color: Color) {
        bufferContext.fillStyle = color.hex
    }

    override fun setFill(gradient: Gradient) {
        when (gradient) {
            is LinearGradient -> {
                val canvasGradient = bufferContext.createLinearGradient(
                    gradient.x0, gradient.y0, gradient.x1, gradient.y1
                )

                for (stop in gradient.stops) {
                    canvasGradient.addColorStop(stop.position, stop.color.hex)
                }

                bufferContext.fillStyle = canvasGradient
            }
        }
    }

    override fun clear() {
        bufferContext.clearRect(0.0, 0.0, receiver.width.toDouble(), receiver.height.toDouble())
    }

    override fun measureText(renderData: CanvasRenderData, text: String): RawTextMeasurement {

        if (text.isEmpty()) {
            return RawTextMeasurement.ZERO
        }

        val textRenderData = renderData.text
        if (textRenderData != null) {
            measureContext.font = textRenderData.toCssString(null)
        }

        val metrics = bufferContext.measureText(text)

        // without the 0.05 Firefox and Chrome displays a '...' as they think that there is not enough space
        // I don't really know why that happens, I guess it's some Double rounding issue

        val width = ceil(metrics.width) + 0.05
        val height = ceil(
            textRenderData?.lineHeight
                ?: textRenderData?.fontSize?.value?.let { it * 1.5 }
                ?: (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent)
        )

        metrics.ideographicBaseline

        return RawTextMeasurement(width, height, metrics.ideographicBaseline)
    }

}