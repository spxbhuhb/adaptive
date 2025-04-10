/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.model.path

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.graphics.canvas.platform.ActualPath

@Adat
class LineTo(
    val x: Double,
    val y: Double
) : PathCommand() {
    override fun apply(path: ActualPath) = path.lineTo(x, y)
}