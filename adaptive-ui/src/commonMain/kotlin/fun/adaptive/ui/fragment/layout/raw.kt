/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.instruction.Surrounding
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CornerRadius
import `fun`.adaptive.ui.instruction.decoration.DropShadow
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.toPx

operator fun Double.plus(other: Double?): Double =
    if (other == null) this else other + this

/**
 * Frame where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this frame.
 */
@Adat
class RawFrame(
    val top: Double,
    val left: Double,
    val width: Double,
    val height: Double
) {
    fun toFrame(adapter: AdaptiveAdapter): Frame {
        if (this === NaF) return Frame.NaF
        check(adapter is DensityIndependentAdapter)
        return Frame(
            adapter.toDp(top),
            adapter.toDp(left),
            adapter.toDp(width),
            adapter.toDp(height)
        )
    }

    fun move(dx: Double, dy: Double): RawFrame =
        RawFrame(top + dy, left + dx, width, height)

    companion object {
        val NaF = RawFrame(Double.NaN, Double.NaN, Double.NaN, Double.NaN)
    }
}

/**
 * Position where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this position.
 */
data class RawPosition(
    val top: Double,
    val left: Double
)

/**
 * Size where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this size.
 */
data class RawSize(
    val width: Double,
    val height: Double
) {
    companion object {
        val ZERO = RawSize(0.0, 0.0)
    }
}

data class RawSurrounding(
    val top: Double,
    val end: Double,
    val bottom: Double,
    val start: Double
) {
    constructor(surrounding: Surrounding, previous: RawSurrounding, adapter: DensityIndependentAdapter) :
        this(
            surrounding.top.toPx(adapter) ?: previous.top,
            surrounding.right.toPx(adapter) ?: previous.end,
            surrounding.bottom.toPx(adapter) ?: previous.bottom,
            surrounding.left.toPx(adapter) ?: previous.start
        )

    companion object {
        val ZERO = RawSurrounding(0.0, 0.0, 0.0, 0.0)
    }
}

data class RawBorder(
    val color: Color,
    val top: Double,
    val right: Double,
    val bottom: Double,
    val left: Double
) {
    constructor(border: Border, previous: RawBorder, adapter: DensityIndependentAdapter) :
        this(
            border.color ?: previous.color,
            border.top.toPx(adapter) ?: previous.top,
            border.right.toPx(adapter) ?: previous.right,
            border.bottom.toPx(adapter) ?: previous.bottom,
            border.left.toPx(adapter) ?: previous.left
        )

    companion object {
        val NONE = RawBorder(Color(0u), 0.0, 0.0, 0.0, 0.0)
    }
}

data class RawCornerRadius(
    val topLeft: Double,
    val topRight: Double,
    val bottomLeft: Double,
    val bottomRight: Double
) {
    constructor(cornerRadius: CornerRadius, previous: RawCornerRadius, adapter: DensityIndependentAdapter) :
        this(
            cornerRadius.topLeft.toPx(adapter) ?: previous.topLeft,
            cornerRadius.topRight.toPx(adapter) ?: previous.topRight,
            cornerRadius.bottomLeft.toPx(adapter) ?: previous.bottomLeft,
            cornerRadius.bottomRight.toPx(adapter) ?: previous.bottomRight
        )

    companion object {
        val ZERO = RawCornerRadius(0.0, 0.0, 0.0, 0.0)
    }
}

data class RawTrack(
    val isFix: Boolean,
    val isFraction: Boolean,
    val rawValue: Double
)

data class RawDropShadow(
    val color: Color,
    val offsetX: Double,
    val offsetY: Double,
    val standardDeviation: Double
) {
    constructor(dropShadow: DropShadow, adapter: DensityIndependentAdapter) :
        this(
            dropShadow.color,
            dropShadow.offsetX.toPx(adapter),
            dropShadow.offsetY.toPx(adapter),
            dropShadow.standardDeviation.toPx(adapter)
        )
}