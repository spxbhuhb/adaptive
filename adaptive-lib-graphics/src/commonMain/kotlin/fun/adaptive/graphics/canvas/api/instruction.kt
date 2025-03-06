package `fun`.adaptive.graphics.canvas.api

import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.canvas.instruction.Stroke
import `fun`.adaptive.ui.instruction.decoration.Color

fun fill(color: Color) = Fill(color)
fun fill(color: Int) = Fill(Color(color.toUInt()))
fun fill(color: UInt) = Fill(Color(color))

fun stroke(color: Color) = Stroke(color)
fun stroke(color: Int) = Stroke(Color(color.toUInt()))
fun stroke(color: UInt) = Stroke(Color(color))
