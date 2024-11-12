/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.svg.instruction.*
import `fun`.adaptive.graphics.svg.instruction.transform.SvgTransform
import `fun`.adaptive.ui.instruction.decoration.Color

class TracingCanvas<T : ActualCanvas>(
    val canvas: T
) : ActualCanvas {

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

    override fun fill() {
        println("fill")
        canvas.fill()
    }

    override fun transform(t: SvgTransform) {
        println("transform: $t")
        canvas.transform(t)
    }

    override fun setFill(color: Color) {
        println("setFill: $color")
        canvas.setFill(color)
    }

    override fun clear() {
        println("clear")
        canvas.clear()
    }
}