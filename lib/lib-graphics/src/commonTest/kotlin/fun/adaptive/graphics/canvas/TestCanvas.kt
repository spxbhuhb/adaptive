/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.graphics.canvas.instruction.CanvasTransformInstruction
import `fun`.adaptive.graphics.canvas.model.gradient.Gradient
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.graphics.canvas.platform.ActualPath
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color

class TestCanvas : ActualCanvas {

    val trace = mutableListOf<String>()

    override val width: Double = Double.NaN
    override val height: Double = Double.NaN

    override fun save(id: Long) {

    }

    override fun restore(id: Long) {

    }

    override fun draw(drawFun: () -> Unit) {

    }

    override fun arc(cx: Double, cy: Double, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean) {

    }

    override fun newPath(): ActualPath {
        return TestPath(this)
    }

    override fun fill(path: ActualPath) {

    }

    override fun stroke(path: ActualPath) {

    }

    override fun fill() {

    }

    override fun fillText(x: Double, y: Double, text: String) {

    }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {

    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {

    }

    override fun image(x: Double, y: Double, width: Double, height: Double, drawFun: ((Int, Int) -> Unit) -> Unit) {

    }

    override fun transform(t: CanvasTransformInstruction) {

    }

    override fun setFont(font: String) {

    }

    override fun setStroke(color: Color) {

    }

    override fun setFill(color: Color) {

    }

    override fun setFill(gradient: Gradient) {

    }

    override fun clear() {

    }

    override fun measureText(renderData: CanvasRenderData, text: String): RawTextMeasurement {
        return RawTextMeasurement(text.length.toDouble(), renderData.text?.fontSize?.value ?: 0.0, 0.0)
    }

}