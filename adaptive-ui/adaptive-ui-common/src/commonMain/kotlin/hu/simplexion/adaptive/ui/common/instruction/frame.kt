/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance

fun frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel) =
    Frame(top, left, width, height)

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
        val NaP = Point(DPixel.NaP, DPixel.NaP)
    }
}

fun size(width: DPixel, height: DPixel): Size =
    Size(width, height)

/**
 * Represents a size of a fragment.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [Size.NaS] to indicate that the size is unspecified (Not a Size).
 */
data class Size(
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.instructedSize = this }
    }

    fun toRaw(adapter : AdaptiveUIAdapter<*,*>) : RawSize =
        RawSize(width.toPx(adapter), height.toPx(adapter))

    fun isNaS() : Boolean =
        (this === NaS)

    companion object {
        val ZERO = Size(0.dp, 0.dp)
        val NaS = Size(DPixel.NaP, DPixel.NaP)
    }
}

fun height(height: DPixel) : Height = Height(height)

/**
 * Set height of a fragment.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [DPixel.NaP] to indicate that the height is unspecified.
 */
data class Height(
    val height: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> {
            it.instructedSize = Size(it.instructedSize.width, this.height)
        }
    }
}

/**
 * Set width of a fragment.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [DPixel.NaP] to indicate that the width is unspecified.
 */
data class Width(
    val width: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> {
            it.instructedSize = Size(width, it.instructedSize.height)
        }
    }

}