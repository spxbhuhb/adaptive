/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.CanvasTransformInstruction
import `fun`.adaptive.graphics.canvas.model.gradient.Gradient
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.utility.dump

class TracingCanvas<T : ActualCanvas>(
    val canvas: T
) : ActualCanvas {

    override val width: Double
        get() = canvas.width

    override val height: Double
        get() = canvas.height

    override var shouldRedraw
        get() = canvas.shouldRedraw
        set(value) {
            canvas.shouldRedraw = value
        }

    override fun setSize(width: Double, height: Double) {
        dump { "setSize: $width, $height" }
        canvas.setSize(width, height)
    }

    override fun redrawNeeded() {
        dump { "redrawNeeded" }
        canvas.redrawNeeded()
    }

    override fun apply(renderData: CanvasRenderData?) {
        dump { "apply: $renderData" }
        super.apply(renderData)
    }

    override fun save(id: Long) {
        dump { "save: $id" }
        canvas.save(id)
    }

    override fun restore(id: Long) {
        dump { "restore: $id" }
        canvas.restore(id)
    }

    override fun draw(drawFun: () -> Unit) {
        dump { "draw" }
        canvas.draw(drawFun)
    }

    override fun arc(cx: Double, cy: Double, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean) {
        dump { "arc: $cx, $cy, $radius, $startAngle, $endAngle, $anticlockwise" }
        canvas.arc(cx, cy, radius, startAngle, endAngle, anticlockwise)
    }

    override fun newPath(): ActualPath {
        dump { "newPath" }
        return canvas.newPath()
    }

    override fun fill(path: ActualPath) {
        dump { "fill: $path" }
        canvas.fill(path)
    }

    override fun stroke(path: ActualPath) {
        dump { "stroke: $path" }
        canvas.stroke(path)
    }

    override fun fill() {
        dump { "fill" }
        canvas.fill()
    }

    override fun fillText(x: Double, y: Double, text: String) {
        dump { "fillText: $x, $y, $text" }
        canvas.fillText(x, y, text)
    }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        dump { "fillRect: $x, $y, $width, $height" }
        canvas.fillRect(x, y, width, height)
    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        dump { "line: $x1, $y1, $x2, $y2" }
        canvas.line(x1, y1, x2, y2)
    }

    override fun image(x: Double, y: Double, width: Double, height: Double, drawFun: ((Int, Int) -> Unit) -> Unit) {
        dump { "image: $x, $y, $width, $height" }
        canvas.image(x, y, width, height, drawFun)
    }

    override fun transform(t: CanvasTransformInstruction) {
        dump { "transform: $t" }
        canvas.transform(t)
    }

    override fun setFont(font: String) {
        dump { "font: $font" }
        canvas.setFont(font)
    }

    override fun setStroke(color: Color) {
        dump { "setStroke: $color" }
        canvas.setStroke(color)
    }

    override fun setFill(color: Color) {
        dump { "setFill: $color" }
        canvas.setFill(color)
    }

    override fun setFill(gradient: Gradient) {
        dump { "setFill: $gradient" }
        canvas.setFill(gradient)
    }

    override fun clear() {
        dump { "clear" }
        canvas.clear()
    }

    override fun measureText(renderData: CanvasRenderData, text: String): RawTextMeasurement {
        dump { "measureText: $text" }
        return canvas.measureText(renderData, text)
    }
}