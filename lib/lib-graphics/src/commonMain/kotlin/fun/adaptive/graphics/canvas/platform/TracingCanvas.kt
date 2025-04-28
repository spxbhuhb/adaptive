/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.CanvasTransformInstruction
import `fun`.adaptive.graphics.canvas.model.gradient.Gradient
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color

class TracingCanvas<T : ActualCanvas>(
    val canvas: T
) : ActualCanvas {

    override val width: Double
        get() = canvas.width

    override val height: Double
        get() = canvas.height

    override fun apply(renderData: CanvasRenderData?) {
        println("apply: $renderData")
        super.apply(renderData)
    }

    override fun save(id: Long) {
        println("save: $id")
        canvas.save(id)
    }

    override fun restore(id: Long) {
        println("restore: $id")
        canvas.restore(id)
    }

    override fun draw(drawFun: () -> Unit) {
        println("draw")
        canvas.draw(drawFun)
    }

    override fun arc(cx: Double, cy: Double, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean) {
        println("arc: $cx, $cy, $radius, $startAngle, $endAngle, $anticlockwise")
        canvas.arc(cx, cy, radius, startAngle, endAngle, anticlockwise)
    }

    override fun newPath(): ActualPath {
        println("newPath")
        return canvas.newPath()
    }

    override fun fill(path: ActualPath) {
        println("fill: $path")
        canvas.fill(path)
    }

    override fun stroke(path: ActualPath) {
        println("stroke: $path")
        canvas.stroke(path)
    }

    override fun fill() {
        println("fill")
        canvas.fill()
    }

    override fun fillText(x: Double, y: Double, text: String) {
        println("fillText: $x, $y, $text")
        canvas.fillText(x, y, text)
    }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        println("fillRect: $x, $y, $width, $height")
        canvas.fillRect(x, y, width, height)
    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        println("line: $x1, $y1, $x2, $y2")
        canvas.line(x1, y1, x2, y2)
    }

    override fun image(x: Double, y: Double, width: Double, height: Double, drawFun: ((Int, Int) -> Unit) -> Unit) {
        println("image: $x, $y, $width, $height")
        canvas.image(x, y, width, height, drawFun)
    }

    override fun transform(t: CanvasTransformInstruction) {
        println("transform: $t")
        canvas.transform(t)
    }

    override fun setFont(font: String) {
        println("font: $font")
        canvas.setFont(font)
    }

    override fun setStroke(color: Color) {
        println("setStroke: $color")
        canvas.setStroke(color)
    }

    override fun setFill(color: Color) {
        println("setFill: $color")
        canvas.setFill(color)
    }

    override fun setFill(gradient: Gradient) {
        println("setFill: $gradient")
        canvas.setFill(gradient)
    }

    override fun clear() {
        println("clear")
        canvas.clear()
    }

    override fun measureText(renderData : CanvasRenderData, text: String): RawTextMeasurement {
        println("measureText: $text")
        return canvas.measureText(renderData, text)
    }
}