/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.graphics.canvas

import hu.simplexion.adaptive.grapics.canvas.ActualPath
import hu.simplexion.adaptive.grapics.svg.instruction.Arc
import org.w3c.dom.Path2D
import kotlin.math.*

class ActualBrowserPath : ActualPath {

    val receiver = Path2D()

    override fun moveTo(x: Double, y: Double) {
        receiver.moveTo(x,y)
    }

    override fun lineTo(x: Double, y: Double) {
        receiver.lineTo(x,y)
    }

    override fun closePath(x1: Double, y1: Double, x2: Double, y2: Double) {
        receiver.closePath()
    }

    override fun arcTo(arc: Arc) {
        var rx = arc.rx
        var ry = arc.ry

        val angleRad = arc.xAxisRotation * (PI / 180.0)
        val dx2 = (arc.x1 - arc.x1) / 2.0
        val dy2 = (arc.y1 - arc.y2) / 2.0

        val x1p = cos(angleRad) * dx2 + sin(angleRad) * dy2
        val y1p = - sin(angleRad) * dx2 + cos(angleRad) * dy2

        val rx2 = rx * rx
        val ry2 = ry * ry
        val x1p2 = x1p * x1p
        val y1p2 = y1p * y1p

        val radiiCheck = (x1p2 / rx2) + (y1p2 / ry2)
        if (radiiCheck > 1) {
            val radiiScale = sqrt(radiiCheck)
            rx *= radiiScale
            ry *= radiiScale
        }

        val sign = if (arc.largeArcFlag == arc.sweepFlag) - 1.0 else 1.0
        val sq = ((rx2 * ry2) - (rx2 * y1p2) - (ry2 * x1p2)) / ((rx2 * y1p2) + (ry2 * x1p2))
        val coef = sign * sqrt(max(sq, 0.0))

        val cxp = coef * (rx * y1p / ry)
        val cyp = coef * - (ry * x1p / rx)

        val cx = cos(angleRad) * cxp - sin(angleRad) * cyp + (arc.x1 + arc.x2) / 2
        val cy = sin(angleRad) * cxp + cos(angleRad) * cyp + (arc.y1 + arc.y2) / 2

        val startAngle = angle((1.0 - (x1p / rx)) / (1.0 - (y1p / ry)))
        val deltaTheta = angle(((- x1p) / rx) / ((- y1p) / ry))

        val endAngle = startAngle + deltaTheta

        receiver.arc(cx, cy, rx, startAngle, endAngle, arc.sweepFlag == 0)
    }

    fun angle(vector: Double): Double {
        return atan2(vector, sqrt(1.0 - vector * vector))
    }

    override fun cubicCurve(x1: Double, y1: Double, x2: Double, y2: Double, x: Double, y: Double) {
        TODO("Not yet implemented")
    }

    override fun smoothCubicCurve(x2: Double, y2: Double, x: Double, y: Double) {
        TODO("Not yet implemented")
    }

    override fun quadraticCurve(x1: Double, y1: Double, x: Double, y: Double) {
        TODO("Not yet implemented")
    }

    override fun smoothQuadraticCurve(x: Double, y: Double) {
        TODO("Not yet implemented")
    }
}