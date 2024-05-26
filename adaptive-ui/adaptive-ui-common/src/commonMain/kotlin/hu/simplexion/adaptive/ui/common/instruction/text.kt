/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.alsoIfInstance

class FontName(val fontName: String) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.fontName = fontName }
    }
}

class FontSize(val fontSize: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.fontSize = fontSize }
    }
}

class FontWeight(val weight: Int) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.fontWeight = weight }
    }
}

class LetterSpacing(val value: Float) : AdaptiveInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.letterSpacing = value }
    }
}

enum class TextWrap : AdaptiveInstruction {
    Wrap,
    NoWrap;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.textWrap = this }
    }
}

enum class TextAlign : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.textAlign = this }
    }
}