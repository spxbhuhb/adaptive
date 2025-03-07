/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.instruction

import `fun`.adaptive.adat.Adat

@Adat
class Translate(
    val tx: Double,
    val ty: Double,
) : CanvasTransformInstruction