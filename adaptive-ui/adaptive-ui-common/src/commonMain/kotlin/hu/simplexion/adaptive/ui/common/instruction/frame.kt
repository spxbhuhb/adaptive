/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

/**
 * Represents a frame on the UI.
 *
 * This class is fully immutable, and we should keep it that way.
 */
data class Frame(
    val point: Point,
    val size: Size
) : AdaptiveInstruction {

    constructor(top: DPixel, left: DPixel, width: DPixel, height: DPixel) :
        this(Point(top, left), Size(width, height))

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> {
            it.instructedPoint = this.point
            it.instructedSize = this.size
        }
    }
}

/**
 * Represents a point on the UI.
 *
 * This class is fully immutable, and we should keep it that way.
 */
data class Point(
    val top: DPixel,
    val left: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.instructedPoint = this }
    }

    companion object {
        val ORIGIN = Point(0.dp, 0.dp)
    }
}

/**
 * Represents a size of an element.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [Size.NaS] to indicate that the size is invalid (Not a Size).
 */
data class Size(
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.instructedSize = this }
    }

    companion object {
        val ZERO = Size(0.dp, 0.dp)
    }
}