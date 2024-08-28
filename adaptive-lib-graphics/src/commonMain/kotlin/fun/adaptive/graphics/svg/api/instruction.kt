package `fun`.adaptive.graphics.svg.api

import `fun`.adaptive.graphics.svg.instruction.Fill
import `fun`.adaptive.ui.instruction.decoration.Color

fun svgFill(color: Int) = Fill(Color(color.toUInt()))