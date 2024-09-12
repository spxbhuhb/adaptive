package `fun`.adaptive.graphics.svg.api

import `fun`.adaptive.graphics.svg.instruction.SvgFill
import `fun`.adaptive.graphics.svg.instruction.SvgHeight
import `fun`.adaptive.graphics.svg.instruction.SvgWidth
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.decoration.Color

fun svgFill(color: Color) = SvgFill(color)
fun svgFill(color: Int) = SvgFill(Color(color.toUInt()))
fun svgFill(color: UInt) = SvgFill(Color(color))

fun svgHeight(height: DPixel) = SvgHeight(height.value)
fun svgWidth(width: DPixel) = SvgWidth(width.value)