/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment.layout

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.*

operator fun Double.plus(other: Double?): Double =
    if (other == null) this else other + this

/**
 * Frame where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this frame.
 */
data class RawFrame(
    val top : Double,
    val left : Double,
    val width : Double,
    val height : Double
) {
    companion object {
        val AUTO = RawFrame(Double.NaN, Double.NaN, Double.NaN, Double.NaN)
    }
}

/**
 * Position where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this position.
 */
data class RawPosition(
    val top: Double,
    val left: Double
) {
    constructor(position: Position, adapter: AbstractCommonAdapter<*, *>) :
        this(position.top.toPx(adapter), position.left.toPx(adapter))
}

/**
 * Size where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this size.
 */
data class RawSize(
    val width: Double,
    val height: Double
) {
    constructor(point: Size, adapter: AbstractCommonAdapter<*, *>) :
        this(point.width.toPx(adapter), point.height.toPx(adapter))

    companion object {
        val UNKNOWN = RawSize(Double.NaN, Double.NaN)
    }
}

data class RawSurrounding(
    val top: Double,
    val end: Double,
    val bottom: Double,
    val start: Double
) {
    constructor(surrounding: Surrounding, previous: RawSurrounding, adapter: AbstractCommonAdapter<*, *>) :
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
    constructor(border: Border, previous: RawBorder, adapter: AbstractCommonAdapter<*, *>) :
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
    constructor(cornerRadius: CornerRadius, previous: RawCornerRadius, adapter: AbstractCommonAdapter<*, *>) :
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
    constructor(dropShadow: DropShadow, adapter: AbstractCommonAdapter<*, *>) :
        this(
            dropShadow.color,
            dropShadow.offsetX.toPx(adapter),
            dropShadow.offsetY.toPx(adapter),
            dropShadow.standardDeviation.toPx(adapter)
        )
}