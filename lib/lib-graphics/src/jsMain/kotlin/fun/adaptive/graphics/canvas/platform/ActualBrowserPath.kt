/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.model.path.Arc
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

    /**
     * Converts SVG arc parameters to HTML Canvas ellipse parameters.
     * Based on the W3C SVG 1.1 Specification, Appendix F.6.
     */
    override fun arcTo(
        arc : Arc
    ) {
        val rx = arc.rx
        val ry = arc.ry
        val xAxisRotation = arc.xAxisRotation
        val largeArcFlag = arc.largeArcFlag
        val sweepFlag = arc.sweepFlag
        val x1 = arc.x1
        val y1 = arc.y1
        val x2 = arc.x2
        val y2 = arc.y2

        val phi_rad = xAxisRotation * (PI / 180) // Convert degrees to radians

        // Ensure radii are positive
        var currentRx = abs(rx)
        var currentRy = abs(ry)

        // Handle degenerate cases
        if (currentRx == 0.0 || currentRy == 0.0) {
            receiver.moveTo(x1, y1)
            receiver.lineTo(x2, y2)
            return
        }
        if (x1 == x2 && y1 == y2) {
            //println("Degenerate arc: Start and end points are identical.")
            return
        }

        // Step 3: Transform endpoints to an aligned coordinate system
        val dx = (x1 - x2) / 2
        val dy = (y1 - y2) / 2

        val x1_prime = cos(phi_rad) * dx + sin(phi_rad) * dy
        val y1_prime = -sin(phi_rad) * dx + cos(phi_rad) * dy

        // Step 4: Adjust radii if too small
        var lambda = (x1_prime * x1_prime) / (currentRx * currentRx) + (y1_prime * y1_prime) / (currentRy * currentRy)
        if (lambda > 1) {
            lambda = sqrt(lambda)
            currentRx *= lambda
            currentRy *= lambda
        }

        // Step 5: Calculate the center of the ellipse in the transformed coordinate system
        val numerator = currentRx * currentRx * currentRy * currentRy - x1_prime * x1_prime * currentRy * currentRy - y1_prime * y1_prime * currentRx * currentRx
        val denominator = x1_prime * x1_prime * currentRy * currentRy + y1_prime * y1_prime * currentRx * currentRx

        var coeff = sqrt(abs(numerator / denominator)) // Use abs for robustness against tiny negative floats

        if (largeArcFlag == sweepFlag) {
            coeff = -coeff
        }

        val cx_prime = coeff * (currentRx * y1_prime / currentRy)
        val cy_prime = coeff * (-currentRy * x1_prime / currentRx)

        // Step 6: Transform the center back to the original coordinate system
        val x_mid = (x1 + x2) / 2
        val y_mid = (y1 + y2) / 2

        val cx = x_mid + cos(phi_rad) * cx_prime - sin(phi_rad) * cy_prime
        val cy = y_mid + sin(phi_rad) * cx_prime + cos(phi_rad) * cy_prime

        // Step 7: Calculate start and end angles
        val start_vector_x = (x1_prime - cx_prime) / currentRx
        val start_vector_y = (y1_prime - cy_prime) / currentRy
        val end_vector_x = (-x1_prime - cx_prime) / currentRx // x2_prime = -x1_prime, y2_prime = -y1_prime in this system
        val end_vector_y = (-y1_prime - cy_prime) / currentRy

        val startAngle = angle(1.0, 0.0, start_vector_x, start_vector_y)
        var endAngle = angle(1.0, 0.0, end_vector_x, end_vector_y)

        // Step 8: Adjust endAngle based on sweepFlag
        // Ensure the arc sweeps in the correct direction
        val twoPI = 2 * PI
        if (sweepFlag == 0) { // Counter-clockwise
            if (endAngle > startAngle) {
                endAngle -= twoPI
            }
        } else { // Clockwise
            if (endAngle < startAngle) {
                endAngle += twoPI
            }
        }

        // Step 9: Determine counterclockwise for Canvas
        val counterclockwise = (sweepFlag == 0)

        receiver.ellipse(
            x = cx,
            y = cy,
            radiusX = currentRx,
            radiusY = currentRy,
            rotation = phi_rad, // xAxisRotation in radians
            startAngle = startAngle,
            endAngle = endAngle,
            anticlockwise = counterclockwise
        )
    }

    /**
     * Helper function to calculate the angle between two vectors.
     * Used in the SVG arc to ellipse conversion.
     *
     * @param ux X component of the first vector.
     * @param uy Y component of the first vector.
     * @param vx X component of the second vector.
     * @param vy Y component of the second vector.
     * @return The angle in radians.
     */
    fun angle(ux: Double, uy: Double, vx: Double, vy: Double): Double {
        val dot = ux * vx + uy * vy
        val magU = sqrt(ux * ux + uy * uy)
        val magV = sqrt(vx * vx + vy * vy)

        // Clamp to avoid floating point errors causing acos to return NaN
        var arg = dot / (magU * magV)
        arg = max(-1.0, min(1.0, arg))

        var a = acos(arg)

        // Determine the sign of the angle
        if (ux * vy - uy * vx < 0) {
            a = -a
        }
        return a
    }

    override fun cubicCurve(x1: Double, y1: Double, x2: Double, y2: Double, x: Double, y: Double) {
        receiver.bezierCurveTo(x1, y1, x2, y2, x, y)
    }

    override fun smoothCubicCurve(x2: Double, y2: Double, x: Double, y: Double) {
        TODO("Not yet implemented")
    }

    override fun quadraticCurve(x1: Double, y1: Double, x: Double, y: Double) {
        receiver.quadraticCurveTo(x1, y1, x, y)
    }

    override fun smoothQuadraticCurve(x: Double, y: Double) {
        TODO("Not yet implemented")
    }
}