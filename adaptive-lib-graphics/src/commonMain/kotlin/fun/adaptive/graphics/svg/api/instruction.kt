package `fun`.adaptive.graphics.svg.api

import `fun`.adaptive.graphics.svg.instruction.SvgHeight
import `fun`.adaptive.graphics.svg.instruction.SvgWidth
import `fun`.adaptive.ui.instruction.DPixel

fun svgHeight(height: DPixel) = SvgHeight(height.value)
fun svgWidth(width: DPixel) = SvgWidth(width.value)