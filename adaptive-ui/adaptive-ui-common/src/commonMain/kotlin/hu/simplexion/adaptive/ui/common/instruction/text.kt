/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

data class FontName(val fontName: String) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontName = fontName }
    }
}

data class FontSize(val fontSize: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontSize = fontSize }
    }
}

data class FontWeight(val weight: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.fontWeight = weight }
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