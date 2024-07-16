/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.fragment.layout.RawBorder
import hu.simplexion.adaptive.ui.common.fragment.layout.RawCornerRadius
import hu.simplexion.adaptive.ui.common.fragment.layout.RawDropShadow
import hu.simplexion.adaptive.ui.common.fragment.layout.RawSurrounding
import hu.simplexion.adaptive.ui.common.render.decoration
import hu.simplexion.adaptive.ui.common.render.layout
import kotlin.math.PI
import kotlin.math.atan2

fun color(color: Int) = Color(color.toUInt())

fun backgroundColor(color: Int) = BackgroundColor(Color(color.toUInt()))
fun backgroundColor(color: UInt) = BackgroundColor(Color(color))
fun backgroundColor(color: Color) = BackgroundColor(color)

fun backgroundGradient(startPosition: Position, endPosition: Position, start: Color, end: Color) = BackgroundGradient(startPosition, endPosition, start, end)
fun leftToRightGradient(leftColor : Color, rightColor: Color) = BackgroundGradient(BackgroundGradient.LEFT, BackgroundGradient.RIGHT, leftColor, rightColor)

fun border(color: Color, width: DPixel = 1.dp) = Border(color, width, width, width, width)
fun borderTop(color: Color, width: DPixel = 1.dp) = Border(color, width, null, null, null)
fun borderRight(color: Color, width: DPixel = 1.dp) = Border(color, null, width, null, null)
fun borderBottom(color: Color, width: DPixel = 1.dp) = Border(color, null, null, width, null)
fun borderLeft(color: Color, width: DPixel = 1.dp) = Border(color, null, null, null, width)

fun cornerRadius(all: DPixel) = CornerRadius(all)
fun cornerRadius(topLeft: DPixel? = null, topRight: DPixel? = null, bottomLeft: DPixel? = null, bottomRight: DPixel? = null) = CornerRadius(topLeft, topRight, bottomLeft, bottomRight)

fun cornerTopRadius(top: DPixel) = cornerRadius(topLeft = top, topRight = top)
fun cornerBottomRadius(bottom: DPixel) = cornerRadius(bottomLeft = bottom, bottomRight = bottom)

fun cornerTopLeftRadius(topLeft: DPixel) = cornerRadius(topLeft = topLeft)
fun cornerTopRightRadius(topRight: DPixel) = cornerRadius(topRight = topRight)
fun cornerBottomLeftRadius(bottomLeft: DPixel) = cornerRadius(bottomLeft = bottomLeft)
fun cornerBottomRightRadius(bottomRight: DPixel) = cornerRadius(bottomRight = bottomRight)

fun dropShadow(color: Color, offsetX: DPixel, offsetY: DPixel, standardDeviation: DPixel) = DropShadow(color, offsetX, offsetY, standardDeviation)

@Adat
class BackgroundColor(
    val color: Color
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        decoration(subject) { it.backgroundColor = color }
    }
}

@Adat
class BackgroundGradient(
    val startPosition: Position,
    val endPosition: Position,
    val start: Color,
    val end: Color
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        decoration(subject) { it.backgroundGradient = this }
    }

    val degree
        get() = asAngle(startPosition, endPosition)

    fun asAngle(startPoint: Position, endPosition: Position): Double {
        val dx = endPosition.left.value - startPoint.left.value
        val dy = endPosition.top.value - startPoint.top.value

        // Calculate the angle in radians
        val angleRadians = atan2(dy, dx)

        // Convert the angle to degrees
        val angleDegrees = angleRadians * 180 / PI

        // Adjust the angle to match CSS coordinate system
        // CSS angles are clockwise from the top (0 degrees)
        var angle = (90 - angleDegrees) % 360
        if (angle < 0) {
            angle += 360
        }

        return angle
    }

    companion object {
        val TOP = Position(0.5.dp, 0.0.dp)
        val BOTTOM = Position(0.5.dp, 1.0.dp)
        val LEFT = Position(0.0.dp, 0.5.dp)
        val RIGHT = Position(1.0.dp, 0.5.dp)
    }
}

@Adat
class Border(
    val color: Color?,
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : AdaptiveInstruction, Surrounding {

    override fun apply(subject: Any) {
        // decided to duplicate the border to keep layout calculations separate from decorations
        layout(subject) {
            it.border = RawSurrounding(this, it.border ?: RawSurrounding.ZERO, it.adapter)
        }
        decoration(subject) {
            it.border = RawBorder(this, it.border ?: RawBorder.NONE, it.adapter)
        }
    }

    companion object {
        val NONE = Border(null, DPixel.ZERO, DPixel.ZERO, DPixel.ZERO, DPixel.ZERO)
    }
}

@Adat
class CornerRadius(
    val topLeft: DPixel?,
    val topRight: DPixel?,
    val bottomLeft: DPixel?,
    val bottomRight: DPixel?
) : AdaptiveInstruction {

    constructor(all: DPixel) : this(all, all, all, all)

    override fun apply(subject: Any) {
        decoration(subject) {
            it.cornerRadius = RawCornerRadius(this, it.cornerRadius ?: RawCornerRadius.ZERO, it.adapter)
        }
    }

}

@Adat
class DropShadow(
    val color: Color,
    val offsetX: DPixel,
    val offsetY: DPixel,
    val standardDeviation: DPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        decoration(subject) { it.dropShadow = RawDropShadow(this, it.adapter) }
    }
}
