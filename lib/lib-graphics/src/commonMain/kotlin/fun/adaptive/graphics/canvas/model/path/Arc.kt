/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.model.path

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.graphics.canvas.platform.ActualPath

/**
 * @property rx X-radius of the ellipse.
 * @property ry Y-radius of the ellipse.
 * @property xAxisRotation Rotation of the ellipse's x-axis in degrees.
 * @property largeArcFlag 0 for small arc, 1 for large arc.
 * @property sweepFlag 0 for counter-clockwise, 1 for clockwise.
 * @property x2 End point X.
 * @property y2 End point Y.
 * @property x1 Start point X, optional, defaults to the end point of the last path operation.
 * @property y1 Start point Y, optional, defaults to the end point of the last path operation.
 *
 * @see https://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
 */
@Adat
class Arc(
    val rx: Double,
    val ry: Double,
    val xAxisRotation: Double,
    val largeArcFlag: Int,
    val sweepFlag: Int,
    val x2: Double,
    val y2: Double,
    val x1: Double? = null,
    val y1: Double? = null,
) : PathCommand() {
    override fun apply(path: ActualPath) = path.arcTo(this)
}

