/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.instruction.*

/**
 * Frame where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this frame.
 */
data class RawFrame(
    val point: RawPoint,
    val size: RawSize
) {
    constructor(frame: Frame, adapter: AdaptiveUIAdapter<*, *>) :
        this(RawPoint(frame.point, adapter), RawSize(frame.size, adapter))

    constructor(top: Float, left: Float, width: Float, height: Float) :
        this(RawPoint(top, left), RawSize(width, height))

    companion object {
        /** Not a frame, indicates the that the frame is not set. **/
        val NaF = RawFrame(RawPoint.NaP, RawSize.NaS)
    }
}

/**
 * Point where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this point.
 */
data class RawPoint(
    val top: Float,
    val left: Float
) {
    constructor(point: Point, adapter: AdaptiveUIAdapter<*, *>) :
        this(point.top.toPx(adapter), point.left.toPx(adapter))

    companion object {
        /** Not a point, indicates the that the point is not set. **/
        val NaP = RawPoint(Float.NaN, Float.NaN)

        val ORIGIN = RawPoint(0f, 0f)
    }
}

/**
 * Size where the values are actual device dependent pixel values. All measurements and
 * layout calculations should use this size.
 */
data class RawSize(
    val width: Float,
    val height: Float
) {
    constructor(point: Size, adapter: AdaptiveUIAdapter<*, *>) :
        this(point.width.toPx(adapter), point.height.toPx(adapter))

    companion object {
        /** Not a size, indicates the that the size is not set. **/
        val NaS = RawSize(Float.NaN, Float.NaN)

        val ZERO = RawSize(0f, 0f)
    }
}

data class RawPadding(
    val top: Float,
    val right: Float,
    val bottom: Float,
    val left: Float
) {
    constructor(padding: Padding, adapter: AdaptiveUIAdapter<*, *>) :
        this(
            padding.top.toPx(adapter),
            padding.right.toPx(adapter),
            padding.bottom.toPx(adapter),
            padding.left.toPx(adapter)
        )
}