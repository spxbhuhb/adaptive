/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.graphics.canvas.platform

import hu.simplexion.adaptive.grapics.canvas.platform.ActualCanvas
import hu.simplexion.adaptive.grapics.canvas.platform.ActualPath
import hu.simplexion.adaptive.grapics.svg.instruction.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI
import kotlin.math.tan

class ActualBrowserCanvas : ActualCanvas {

    val receiver = (document.createElement("canvas") as HTMLCanvasElement).also {
        it.style.imageRendering = "crisp-edges"
    }

    val context = (receiver.getContext("2d") as CanvasRenderingContext2D)

    var saveIds = mutableListOf<Long>()

    fun setSize(width: Double, height: Double) {
        val scale = window.devicePixelRatio
        receiver.width = (width * scale).toInt()
        receiver.height = (height * scale).toInt()
        context.scale(scale, scale)
    }

    override fun startDraw() {

    }

    override fun endDraw() {
    }

    override fun save(id: Long) {
        if (saveIds.lastOrNull() == id) return
        saveIds.add(id)
        context.save()
    }

    override fun restore(id: Long) {
        if (saveIds.lastOrNull() != id) return
        saveIds.removeLastOrNull()
        context.restore()
    }

    override fun newPath(): ActualBrowserPath {
        return ActualBrowserPath()
    }

    override fun fill(path: ActualPath) {
        path as ActualBrowserPath
        context.fill(path.receiver)
    }

    override fun transform(t: SvgTransform) {
        when (t) {
            is Translate -> context.translate(t.tx, t.ty)

            is Rotate -> {
                context.translate(- t.cx, - t.cy)
                context.rotate(t.rotateAngle)
                context.translate(t.cx, t.cy)
            }

            is Scale -> context.scale(t.sx, t.sy)

            is Matrix -> context.transform(t.a, t.b, t.c, t.d, t.e, t.f)

            is SkewX -> {
                val rad = t.skewAngle * PI / 180
                context.transform(1.0, 0.0, tan(rad), 1.0, 0.0, 0.0)
            }

            is SkewY -> {
                val rad = t.skewAngle * PI / 180
                context.transform(1.0, tan(rad), 0.0, 1.0, 0.0, 0.0)
            }
        }
    }

    override fun setFill(fill: Fill) {
        context.fillStyle = fill.fill
    }
}