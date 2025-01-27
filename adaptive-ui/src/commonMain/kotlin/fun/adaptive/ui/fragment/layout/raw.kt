/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CornerRadius
import `fun`.adaptive.ui.instruction.decoration.DropShadow

operator fun Double.plus(other: Double?): Double =
    if (other == null) this else other + this

/**
 * Frame where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this frame.
 */
@Adat
class RawFrame(
    val top : Double,
    val left : Double,
    val width : Double,
    val height : Double
)

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
)

data class RawSurrounding(
    val top: Double,
    val end: Double,
    val bottom: Double,
    val start: Double
) {
    constructor(surrounding: Surrounding, previous: RawSurrounding, adapter: AbstractAuiAdapter<*, *>) :
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
    constructor(border: Border, previous: RawBorder, adapter: AbstractAuiAdapter<*, *>) :
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
    constructor(cornerRadius: CornerRadius, previous: RawCornerRadius, adapter: AbstractAuiAdapter<*, *>) :
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
    constructor(dropShadow: DropShadow, adapter: AbstractAuiAdapter<*, *>) :
        this(
            dropShadow.color,
            dropShadow.offsetX.toPx(adapter),
            dropShadow.offsetY.toPx(adapter),
            dropShadow.standardDeviation.toPx(adapter)
        )
}