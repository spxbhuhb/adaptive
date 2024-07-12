/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.text
import hu.simplexion.adaptive.ui.common.render.textAndAdapter

fun fontName(fontName: String) = FontName(fontName)
inline fun fontName(fontName: () -> String) = FontName(fontName())

fun fontSize(fontSize: SPixel) = FontSize(fontSize)
inline fun fontSize(fontSize: () -> SPixel) = FontSize(fontSize())

fun fontWeight(weight: Int) = FontWeight(weight)
inline fun fontWeight(fontWeight: () -> Int) = FontWeight(fontWeight())

fun lineHeight(height: DPixel) = LineHeight(height)
inline fun lineHeight(height: () -> DPixel) = LineHeight(height())

val noSelect = NoSelect()
val noWrap = TextWrap(false)
val underline = TextUnderline()

val bold = FontWeight.BOLD

val smallCaps = TextSmallCaps()

fun letterSpacing(value: Double) = LetterSpacing(value)

fun textColor(value: Int) = TextColor(Color(value.toUInt()))
fun textColor(value: UInt) = TextColor(Color(value))
fun textColor(value: Color) = TextColor(value)

@Adat
class FontName(
    val fontName: String
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontName = fontName }
    }
}

@Adat
class FontSize(
    val fontSize: SPixel
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontSize = fontSize }
    }
}

@Adat
class FontWeight(
    val weight: Int
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontWeight = weight }
    }

    companion object {
        val THIN = FontWeight(100)
        val EXTRA_LIGHT = FontWeight(200)
        val LIGHT = FontWeight(300)
        val NORMAL = FontWeight(400)
        val MEDIUM = FontWeight(500)
        val SEMI_BOLD = FontWeight(600)
        val BOLD = FontWeight(700)
        val EXTRA_BOLD = FontWeight(800)
        val BLACK = FontWeight(900)
    }
}

@Adat
class TextItalic : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.italic = true }
    }
}

@Adat
class TextSmallCaps : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.smallCaps = true }
    }
}

@Adat
class TextUnderline : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.underline = true }
    }
}

@Adat
class NoSelect : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.noSelect = true }
    }
}

@Adat
class LetterSpacing(val value: Double) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.letterSpacing = value }
    }
}

@Adat
class LineHeight(val height: DPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        textAndAdapter(subject) { t, a -> t.lineHeight = a.toPx(height) }
    }
}

@Adat
class TextWrap(
    val wrap: Boolean
) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.wrap = wrap }
    }
}

@Adat
class TextColor(
    val color: Color
) : AdaptiveInstruction {

    constructor(color: UInt) : this(Color(color))

    override fun apply(subject: Any) {
        text(subject) { it.color = this.color }
    }
}