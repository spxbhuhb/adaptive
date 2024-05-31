/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class BackgroundColor(val color: Color) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.backgroundColor = color }
    }
}

data class BackgroundGradient(val degree: Float, val start: Color, val end: Color) : AdaptiveInstruction {

    constructor(degree: Int, start : Color, end : Color) : this(degree.toFloat(), start, end)

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.backgroundGradient = this }
    }

}
