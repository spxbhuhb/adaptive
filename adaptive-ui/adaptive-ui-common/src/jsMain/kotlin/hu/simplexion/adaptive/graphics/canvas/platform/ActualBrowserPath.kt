/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.graphics.canvas.platform

import hu.simplexion.adaptive.grapics.canvas.platform.ActualPath
import hu.simplexion.adaptive.grapics.svg.instruction.Arc
import org.w3c.dom.Path2D
import kotlin.math.*

class ActualBrowserPath : ActualPath {

    val receiver = Path2D()

    override fun moveTo(x: Double, y: Double) {
        receiver.moveTo(x, y)
    }

    override fun lineTo(x: Double, y: Double) {
        receiver.lineTo(x, y)
    }

    override fun closePath(x1: Double, y1: Double, x2: Double, y2: Double) {
        receiver.closePath()
    }

    override fun arcTo(arc: Arc) {
        val x1 = arc.x1
        val y1 = arc.y1
        val x2 = arc.x2
        val y2 = arc.y2
        var rx = arc.rx
        var ry = arc.ry

        val rxS = rx * rx
        val ryS = ry * ry

        require(rx > 0 && ry > 0) { "rx or ry is <= 0" }

        val p = arc.xAxisRotation / 180 * PI
        val xM = (x1 - x2) / 2
        val yM = (y1 - y2) / 2

        val x1p = cos(p) * xM + sin(p) * yM
        val y1p = - sin(p) * xM + cos(p) * yM

        val x1pS = x1p * x1p
        val y1pS = y1p * y1p

        val radiusCheckValue = x1pS / rxS + y1pS / ryS
        if (radiusCheckValue > 1) {
            // Radius is too small to build an arc!
            // Check out radius correction in the W3C document
            // https://www.w3.org/TR/SVG11/implnote.html#ArcCorrectionOutOfRangeRadii
            val rr = sqrt(radiusCheckValue)
            rx *= rr
            ry *= rr
        }

        val s = if (arc.largeArcFlag != arc.sweepFlag) 1 else - 1

        // the abs is here because Double may act strange and result in -1.4210854715202004e-14
        // that is basically zero, but sqrt returns with NaN because it's negative
        val sq = sqrt(abs(rxS * ryS - rxS * y1pS - ryS * x1pS) / (rxS * y1pS + ryS * x1pS))

        val cxp = s * sq * rx * y1p / ry
        val cyp = s * sq * - ry * x1p / rx

        val xmd = (x1 + x2) / 2
        val ymd = (y1 + y2) / 2
        val cx = cos(p) * cxp - sin(p) * cyp + xmd
        val cy = sin(p) * cxp + cos(p) * cyp + ymd

        val startAngle = angle(1.0, 0.0, x1 - cx, y1 - cy) - p
        val deltaAngle = angle(x1 - cx, y1 - cy, x2 - cx, y2 - cy)

        receiver.ellipse(cx, cy, rx, ry, p, startAngle, startAngle + deltaAngle, arc.sweepFlag == 0)
    }

    fun angle(ux: Double, uy: Double, vx: Double, vy: Double) =
        (if (ux * vy >= uy * vx) 1 else - 1) *
            acos((ux * vx + uy * vy) / (sqrt(ux * ux + uy * uy) * sqrt(vx * vx + vy * vy)))

    override fun cubicCurve(x1: Double, y1: Double, x2: Double, y2: Double, x: Double, y: Double) {
        receiver.bezierCurveTo(x1, y1, x2, y2, x, y)
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