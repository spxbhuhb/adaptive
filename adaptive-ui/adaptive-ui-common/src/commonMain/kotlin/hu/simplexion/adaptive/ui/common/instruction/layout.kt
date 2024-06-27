/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.container
import hu.simplexion.adaptive.ui.common.render.layout
import hu.simplexion.adaptive.ui.common.support.layout.RawSurrounding

fun frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel) = Frame(top, left, width, height)
fun position(top: DPixel, left: DPixel) = Position(top, left)
fun size(width: DPixel, height: DPixel) = Size(width, height)

fun height(height: DPixel) = Height(height)
fun height(calc: () -> DPixel) = Height(calc())

fun width(width: DPixel) = Width(width)
fun width(calc: () -> DPixel) = Width(calc())

object fillSpace : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.fillHorizontal = true
            it.fillVertical = true
        }
    }
}

object fillHorizontal : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.fillHorizontal = true
        }
    }
}

object fillVertical : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.fillVertical = true
        }
    }
}

fun gap(calcBoth: () -> DPixel) = calcBoth().let { Gap(it, it) }
fun gap(both: DPixel) = Gap(both, both)
fun gap(width: DPixel? = null, height: DPixel? = null) = Gap(width, height)

fun gapHeight(height: () -> DPixel) = Gap(height = height(), width = null)
fun gapHeight(height: DPixel) = Gap(height = height, width = null)

fun gapWidth(width: () -> DPixel) = Gap(width = width(), height = null)
fun gapWidth(width: DPixel) = Gap(width = width, height = null)

fun padding(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null) = Padding(top, right, bottom, left)
fun padding(all: () -> DPixel) = Padding(all())
fun padding(all: DPixel) = Padding(all)
fun paddingTop(top: DPixel) = padding(top = top)
fun paddingRight(right: DPixel) = padding(right = right)
fun paddingBottom(bottom: DPixel) = padding(bottom = bottom)
fun paddingLeft(left: DPixel) = padding(left = left)

fun margin(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null) = Margin(top, right, bottom, left)
fun margin(all: () -> DPixel) = Margin(all())
fun margin(all: DPixel) = Margin(all)
fun marginTop(top: DPixel) = margin(top = top)
fun marginRight(right: DPixel) = margin(right = right)
fun marginBottom(bottom: DPixel) = margin(bottom = bottom)
fun marginLeft(left: DPixel) = margin(left = left)

val spaceAround = DistributeSpace(SpaceDistribution.Around)
val spaceBetween = DistributeSpace(SpaceDistribution.Between)

val scroll = Scroll(horizontal = true, vertical = true)
val verticalScroll = Scroll(horizontal = true, vertical = true)
val horizontalScroll = Scroll(horizontal = true, vertical = true)

data class Frame(
    val top: DPixel,
    val left: DPixel,
    val width: DPixel,
    val height: DPixel
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        layout(subject) {
            val adapter = it.adapter
            it.instructedTop = adapter.toPx(top)
            it.instructedLeft = adapter.toPx(left)
            it.instructedWidth = adapter.toPx(width)
            it.instructedHeight = adapter.toPx(height)
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
            it.instructedTop = adapter.toPx(top)
            it.instructedLeft = adapter.toPx(left)
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
            it.instructedWidth = adapter.toPx(width)
            it.instructedHeight = adapter.toPx(height)
        }
    }
}

data class Height(
    val height: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.instructedHeight = it.adapter.toPx(height)
        }
    }
}

data class Width(
    val width: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        layout(subject) {
            it.instructedWidth = it.adapter.toPx(width)
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

enum class Alignment {
    Start,
    Center,
    End
}

data class AlignItems(
    val vertical: Alignment?,
    val horizontal: Alignment?,
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        container(subject) {
            if (vertical != null) it.verticalAlignment = vertical
            if (horizontal != null) it.horizontalAlignment = horizontal
        }
    }

    companion object {
        val alignItems = AlignItems

        val center = AlignItems(vertical = Alignment.Center, horizontal = Alignment.Center)

        val top = AlignItems(vertical = Alignment.Start, horizontal = null)

        val topStart = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Start)
        val topCenter = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Center)
        val topEnd = AlignItems(vertical = Alignment.Start, horizontal = Alignment.End)

        val start = AlignItems(vertical = null, horizontal = Alignment.Start)

        val startTop = AlignItems(vertical = Alignment.Start, horizontal = Alignment.Start)
        val startCenter = AlignItems(vertical = Alignment.Center, horizontal = Alignment.Start)
        val startBottom = AlignItems(vertical = Alignment.End, horizontal = Alignment.Start)

        val end = AlignItems(vertical = null, horizontal = Alignment.End)

        val endTop = AlignItems(vertical = Alignment.Start, horizontal = Alignment.End)
        val endCenter = AlignItems(vertical = Alignment.Center, horizontal = Alignment.End)
        val endBottom = AlignItems(vertical = Alignment.End, horizontal = Alignment.End)

        val bottom = AlignItems(vertical = Alignment.End, horizontal = null)

        val bottomStart = AlignItems(vertical = Alignment.End, horizontal = Alignment.Start)
        val bottomCenter = AlignItems(vertical = Alignment.End, horizontal = Alignment.Center)
        val bottomEnd = AlignItems(vertical = Alignment.End, horizontal = Alignment.End)
    }
}

data class AlignSelf(
    val vertical: Alignment?,
    val horizontal: Alignment?,
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        layout(subject) {
            if (vertical != null) it.verticalAlignment = vertical
            if (horizontal != null) it.horizontalAlignment = horizontal
        }
    }

    companion object {
        val alignSelf = AlignSelf

        val center = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.Center)

        val top = AlignSelf(vertical = Alignment.Start, horizontal = null)

        val topStart = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Start)
        val topCenter = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Center)
        val topEnd = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.End)

        val start = AlignSelf(vertical = null, horizontal = Alignment.Start)

        val startTop = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.Start)
        val startCenter = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.Start)
        val startBottom = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Start)

        val end = AlignSelf(vertical = null, horizontal = Alignment.End)

        val endTop = AlignSelf(vertical = Alignment.Start, horizontal = Alignment.End)
        val endCenter = AlignSelf(vertical = Alignment.Center, horizontal = Alignment.End)
        val endBottom = AlignSelf(vertical = Alignment.End, horizontal = Alignment.End)

        val bottom = AlignSelf(vertical = Alignment.End, horizontal = null)

        val bottomStart = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Start)
        val bottomCenter = AlignSelf(vertical = Alignment.End, horizontal = Alignment.Center)
        val bottomEnd = AlignSelf(vertical = Alignment.End, horizontal = Alignment.End)
    }
}

enum class SpaceDistribution {
    Between,
    Around
}

data class DistributeSpace(
    val distribution: SpaceDistribution
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        container(subject) {
            it.spaceDistribution = distribution
        }
    }
}

data class Scroll(
    val horizontal: Boolean? = null,
    val vertical: Boolean? = null
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        container(subject) {
            if (vertical != null) it.verticalScroll = vertical
            if (horizontal != null) it.horizontalScroll = horizontal
        }
    }
}