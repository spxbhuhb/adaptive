/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.instruction

import hu.simplexion.adaptive.grapics.canvas.ActualPath

interface SvgPathCommand {
    fun apply(path: ActualPath)
}

class MoveTo(
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.moveTo(x, y)
}

class ClosePath(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.closePath(x1, y1, x2, y2)
}

class LineTo(
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.lineTo(x, y)
}

class CubicCurve(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.cubicCurve(x1, y1, x2, y2, x, y)
}

class CubicCurveSmooth(
    val x2: Double,
    val y2: Double,
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.smoothCubicCurve(x2, y2, x, y)
}

class QuadraticCurve(
    val x1: Double,
    val y1: Double,
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.quadraticCurve(x1, y1, x, y)
}

class QuadraticCurveSmooth(
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.smoothQuadraticCurve(x, y)
}

class Arc(
    val rx: Double,
    val ry: Double,
    val xAxisRotation: Double,
    val largeArcFlag: Int,
    val sweepFlag: Int,
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.arcTo(this)
}

