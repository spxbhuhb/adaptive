/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.adat.Adat

@Adat
class Matrix(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double,
    val e: Double,
    val f: Double
) : CanvasTransformInstruction