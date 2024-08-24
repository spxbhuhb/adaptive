/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.svg.instruction.Arc

interface ActualPath {
    fun moveTo(x: Double, y: Double)
    fun lineTo(x: Double, y: Double)
    fun closePath(x1: Double, y1: Double, x2: Double, y2: Double)
    fun arcTo(arc : Arc)
    fun cubicCurve(x1: Double, y1: Double, x2: Double, y2: Double, x: Double, y: Double)
    fun smoothCubicCurve(x2: Double, y2: Double, x: Double, y: Double)
    fun quadraticCurve(x1: Double, y1: Double, x: Double, y: Double)
    fun smoothQuadraticCurve(x: Double, y: Double)
}