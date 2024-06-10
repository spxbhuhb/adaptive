/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.coroutines.channels.Channel

data class FontName(val fontName: String) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontName = fontName }
    }
}

data class FontSize(val fontSize: SPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontSize = fontSize }
    }
}

data class FontWeight(val weight: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontWeight = weight }
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

val noSelect = NoSelect()

class NoSelect : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.noSelect = true }
    }
}

data class LetterSpacing(val value: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.letterSpacing = value }
    }
}

enum class TextWrap : AdaptiveInstruction {
    Wrap,
    NoWrap;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.textWrap = this }
    }
}

enum class TextAlign : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.textAlign = this }
    }
}