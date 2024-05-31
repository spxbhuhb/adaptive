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
 *
 * Use [Frame.NaF] to indicate that the point is invalid (Not a Point).
 */
data class Frame(
    val point: Point,
    val size: Size
) : AdaptiveInstruction {

    constructor(top: Float, left: Float, width: Float, height: Float) : this(Point(top, left), Size(width, height))

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> {
            it.instructedPoint = this.point
            it.instructedSize = this.size
        }
    }

    companion object {
        /** Not a frame, indicates the that the frame is not set. **/
        val NaF = Frame(Point.NaP, Size.NaS)
    }
}

/**
 * Represents a point on the UI.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [Point.NaP] to indicate that the point is invalid (Not a Point).
 */
data class Point(
    val top: Float,
    val left: Float
) : AdaptiveInstruction {

    constructor(top: Int, left: Int) : this(top.toFloat(), left.toFloat())

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.instructedPoint = this }
    }

    companion object {
        /** Not a point, indicates the that the point is not set. **/
        val NaP = Point(Float.NaN, Float.NaN)

        val ORIGIN = Point(0f, 0f)
    }
}

/**
 * Represents a size of an element.
 *
 * This class is fully immutable, and we should keep it that way.
 *
 * Use [Size.NaS] to indicate that the size is invalid (Not a Size).
 */
data class Size(val width: Float, val height: Float) : AdaptiveInstruction {

    constructor(width: Int, height: Int) : this(width.toFloat(), height.toFloat())

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.instructedSize = this }
    }

    companion object {
        /** Not a size, indicates the that the size is not set. **/
        val NaS = Size(Float.NaN, Float.NaN)
    }
}