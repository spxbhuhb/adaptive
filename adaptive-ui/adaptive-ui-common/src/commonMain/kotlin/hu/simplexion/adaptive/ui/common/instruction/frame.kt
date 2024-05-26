/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.alsoIfInstance

/**
 * Represents a rectangular frame.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [Frame.NaF] to indicate that the frame is invalid (Not a Frame).
 */
data class Frame(
    val top: Float,
    val left: Float,
    val width: Float,
    val height: Float
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.frame = this }
    }

    companion object {
        /** Not a frame, indicates the that the frame is not set. **/
        val NaF = Frame(Float.NaN, Float.NaN, Float.NaN, Float.NaN)
    }
}

data class Size(val width: Float, val height: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.size = this }
    }
}