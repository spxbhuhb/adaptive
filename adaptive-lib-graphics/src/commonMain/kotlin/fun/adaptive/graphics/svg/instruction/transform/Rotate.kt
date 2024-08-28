/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction.transform

data class Rotate(
    val rotateAngle: Double,
    val cx: Double,
    val cy: Double
) : SvgTransform