/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.path

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.graphics.canvas.platform.ActualPath

@Adat
class QuadraticCurve(
    val x1: Double,
    val y1: Double,
    val x: Double,
    val y: Double
) : PathCommand() {
    override fun apply(path: ActualPath) = path.quadraticCurve(x1, y1, x, y)
}