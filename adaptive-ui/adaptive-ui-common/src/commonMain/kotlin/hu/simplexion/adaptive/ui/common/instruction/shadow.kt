/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

fun dropShadow(
    color: Color,
    offsetX: DPixel,
    offsetY: DPixel,
    standardDeviation: DPixel
) = DropShadow(color, offsetX, offsetY, standardDeviation)

data class DropShadow(
    val color: Color,
    val offsetX: DPixel,
    val offsetY: DPixel,
    val standardDeviation: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.dropShadow = this }
    }

}
