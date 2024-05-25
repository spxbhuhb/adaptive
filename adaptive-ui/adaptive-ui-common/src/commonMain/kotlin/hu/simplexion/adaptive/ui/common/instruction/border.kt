/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.alsoIfInstance

class BorderRadius(val radius: Float) : AdaptiveInstruction {

    constructor(radius: Int) : this(radius.toFloat())

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.borderRadius = radius }
    }
}

class Border(val color: Color, val width: Float = 1f) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.border = this }
    }
}
