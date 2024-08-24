/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction

import `fun`.adaptive.graphics.canvas.platform.ActualPath

interface SvgPathCommand {
    fun apply(path: ActualPath)
}

data class MoveTo(
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.moveTo(x, y)
}

data class ClosePath(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.closePath(x1, y1, x2, y2)
}

data class LineTo(
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.lineTo(x, y)
}

enum class BezierCurveType {
    Cubic,
    Quadratic
}

data class BezierCurve(
    val type : BezierCurveType,
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.cubicCurve(x1, y1, x2, y2, x, y)
}

data class QuadraticCurve(
    val x1: Double,
    val y1: Double,
    val x: Double,
    val y: Double
) : SvgPathCommand {
    override fun apply(path: ActualPath) = path.quadraticCurve(x1, y1, x, y)
}

data class Arc(
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

