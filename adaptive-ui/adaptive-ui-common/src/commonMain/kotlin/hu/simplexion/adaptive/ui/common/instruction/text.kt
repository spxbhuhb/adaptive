/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class FontName(val fontName: String) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.fontName = fontName }
    }
}

data class FontSize(val fontSize: SPixel) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.fontSize = fontSize }
    }
}

data class FontWeight(val weight: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.fontWeight = weight }
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

enum class TextDecoration(val value : String) : AdaptiveInstruction {
    None("none"),
    Underline("underline");

    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.textDecoration = this }
    }
}

val noSelect = NoSelect()

class NoSelect : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.noSelect = true }
    }
}

data class LetterSpacing(val value: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.letterSpacing = value }
    }
}

enum class TextWrap : AdaptiveInstruction {
    Wrap,
    NoWrap;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.textWrap = this }
    }
}

enum class TextAlign : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<CommonRenderData> { it.textAlign = this }
    }
}