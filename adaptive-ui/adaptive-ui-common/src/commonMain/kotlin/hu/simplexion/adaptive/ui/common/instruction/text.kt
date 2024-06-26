/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

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
val bold = FontWeight.BOLD

fun letterSpacing(value : Double) = LetterSpacing(value)

data class FontName(val fontName: String) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontName = fontName }
    }
}

data class FontSize(val fontSize: SPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.fontSize = fontSize }
    }
}

data class FontWeight(val weight: Int) : AdaptiveInstruction {
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

enum class FontStyle(val value: String) : AdaptiveInstruction {
    Normal("normal"),
    Italic("italic");

    override fun apply(subject: Any) {
        text(subject) { it.fontStyle = this }
    }
}

enum class FontVariant(val value: String) : AdaptiveInstruction {
    Normal("normal"),
    SmallCaps("small-caps");

    override fun apply(subject: Any) {
        text(subject) { it.fontVariant = this }
    }
}

enum class TextDecoration(val value : String) : AdaptiveInstruction {
    None("none"),
    Underline("underline");

    override fun apply(subject: Any) {
        text(subject) { it.decoration = this }
    }
}

class NoSelect : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.noSelect = true }
    }
}

data class LetterSpacing(val value: Double) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        text(subject) { it.letterSpacing = value }
    }
}

data class LineHeight(val height: DPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        textAndAdapter(subject) { t, a -> t.lineHeight = a.toPx(height) }
    }
}

enum class TextWrap : AdaptiveInstruction {
    Wrap,
    NoWrap;

    override fun apply(subject: Any) {
        text(subject) { it.wrap = this }
    }
}

enum class TextAlign : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        text(subject) { it.align = this }
    }
}


data class Color(val value: UInt) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        text(subject) { it.color = this }
    }

    /**
     * @return [value] in "#ffffff" format
     */
    fun toHexColor(): String =
        "#${value.toString(16).padStart(6, '0')}"

}