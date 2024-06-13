/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.grapics.canvas

interface Command {
    fun draw(path: ActualPath)
}

class MoveTo(
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.moveTo(x, y)
}

class ClosePath(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
) : Command {
    override fun draw(path: ActualPath) = path.closePath(x1, y1, x2, y2)
}

class LineTo(
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.lineTo(x, y)
}

class CubicCurve(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.cubicCurve(x1, y1, x2, y2, x, y)
}

class CubicCurveSmooth(
    val x2: Float,
    val y2: Float,
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.smoothCubicCurve(x2, y2, x, y)
}

class QuadraticCurve(
    val x1: Float,
    val y1: Float,
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.quadraticCurve(x1, y1, x, y)
}

class QuadraticCurveSmooth(
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.smoothQuadraticCurve(x, y)
}

class Arc(
    val rx: Float,
    val ry: Float,
    val xAxisRotation: Float,
    val largeArcFlag: Int,
    val sweepFlag: Int,
    val x: Float,
    val y: Float
) : Command {
    override fun draw(path: ActualPath) = path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y)
}

