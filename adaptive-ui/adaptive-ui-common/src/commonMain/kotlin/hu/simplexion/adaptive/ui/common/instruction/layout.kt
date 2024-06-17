/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.container
import hu.simplexion.adaptive.ui.common.render.layout
import hu.simplexion.adaptive.ui.common.support.RawSurrounding

fun frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel) = Frame(top, left, width, height)
fun position(top: DPixel, left: DPixel) = Position(left, top)
fun size(width: DPixel, height: DPixel) = Size(width, height)
fun height(height: DPixel) = Height(height)
fun width(width: DPixel) = Width(width)

val sizeFull = Size(DPixel.FULL, DPixel.FULL)
val heightFull = Height(DPixel.FULL)
val widthFull = Width(DPixel.FULL)

fun gap(both: DPixel) = Gap(both, both)
fun gap(width: DPixel? = null, height: DPixel? = null) = Gap(width, height)

fun padding(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null) = Padding(top, right, bottom, left)
fun padding(all: DPixel) = Padding(all)
fun paddingTop(top: DPixel) = padding(top = top)
fun paddingRight(right: DPixel) = padding(right = right)
fun paddingBottom(bottom: DPixel) = padding(bottom = bottom)
fun paddingLeft(left: DPixel) = padding(left = left)

fun margin(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null) = Margin(top, right, bottom, left)
fun margin(all: DPixel) = Margin(all)
fun marginTop(top: DPixel) = margin(top = top)
fun marginRight(right: DPixel) = margin(right = right)
fun marginBottom(bottom: DPixel) = margin(bottom = bottom)
fun marginLeft(left: DPixel) = margin(left = left)

data class Frame(
    val top: DPixel,
    val left: DPixel,
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.top = adapter.toPx(top)
            it.left = adapter.toPx(left)
            it.width = adapter.toPx(width)
            it.height = adapter.toPx(height)
        }
    }
}

data class Position(
    val top: DPixel,
    val left: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.top = adapter.toPx(top)
            it.left = adapter.toPx(left)
        }
    }
}

data class Size(
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.width = adapter.toPx(width)
            it.height = adapter.toPx(height)
        }
    }
}

data class Height(
    val height: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.height = it.adapter.toPx(height)
        }
    }
}

data class Width(
    val width: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.width = it.adapter.toPx(width)
        }
    }
}

data class Gap(
    val width: DPixel?,
    val height: DPixel?
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        container(subject) { c ->
            c.gapWidth = width?.let { c.adapter.toPx(it) } ?: c.gapWidth
            c.gapHeight = height?.let { c.adapter.toPx(it) } ?: c.gapHeight
        }
    }
}

data class Padding(
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : AdaptiveInstruction, Surrounding {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        layout(subject) {
            it.padding = RawSurrounding(this, it.padding ?: RawSurrounding.ZERO, it.adapter)
        }
    }

}

data class Margin(
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : AdaptiveInstruction, Surrounding {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        layout(subject) {
            it.margin = RawSurrounding(this, it.margin ?: RawSurrounding.ZERO, it.adapter)
        }
    }

}