/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.graphics.canvas.platform.ActualPath
import `fun`.adaptive.graphics.svg.instruction.SvgFill
import `fun`.adaptive.graphics.svg.instruction.transform.SvgTransform
import `fun`.adaptive.ui.instruction.decoration.Color

class TestCanvas : ActualCanvas {

    val trace = mutableListOf<String>()

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

    override fun fill() {

    }

    override fun fillText(x: Double, y: Double, text: String) {

    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        TODO("Not yet implemented")
    }

    override fun transform(t: SvgTransform) {

    }

    override fun setFont(font: String) {
        TODO("Not yet implemented")
    }

    override fun setStroke(color: Color) {
        TODO("Not yet implemented")
    }

    override fun setFill(color: Color) {
        TODO("Not yet implemented")
    }

    override fun clear() {

    }

}