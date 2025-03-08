package `fun`.adaptive.graphics.canvas.api

import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.canvas.instruction.Matrix
import `fun`.adaptive.graphics.canvas.instruction.Rotate
import `fun`.adaptive.graphics.canvas.instruction.Scale
import `fun`.adaptive.graphics.canvas.instruction.SkewX
import `fun`.adaptive.graphics.canvas.instruction.SkewY
import `fun`.adaptive.graphics.canvas.instruction.Stroke
import `fun`.adaptive.graphics.canvas.instruction.Translate
import `fun`.adaptive.ui.instruction.decoration.Color

fun fill(color: Color) = Fill(color)
fun fill(color: Int) = Fill(Color(color.toUInt()))
fun fill(color: UInt) = Fill(Color(color))

fun stroke(color: Color) = Stroke(color)
fun stroke(color: Int) = Stroke(Color(color.toUInt()))
fun stroke(color: UInt) = Stroke(Color(color))

fun translate(x: Double, y: Double) = Translate(x, y)
fun rotate(angle: Double, x: Double = 0.0, y: Double = 0.0) = Rotate(angle, x, y)
fun scale(x: Double, y: Double) = Scale(x, y)
fun skewX(angle: Double) = SkewX(angle)
fun skewY(angle: Double) = SkewY(angle)
fun matrix(a: Double, b: Double, c: Double, d: Double, e: Double, f: Double) = Matrix(a, b, c, d, e, f)
