package `fun`.adaptive.ui.codefence

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class CodeFenceTheme {

    val lineHeight = 17.dp
    val codeLineTopPadding = 3.dp
    val containerTopPadding = 3.dp
    val containerBottomPadding = 6.dp
    val borderWidth = 1.dp

    fun height(fragment : AdaptiveFragment, lineCount: Int): AdaptiveInstruction {
        val instructions = fragment.instructions

        if (instructions.firstInstanceOfOrNull<Height>() != null) return nop
        if (instructions.firstInstanceOfOrNull<Size>() != null) return nop

        return height(
            DPixel(
                containerTopPadding.value + containerBottomPadding.value + borderWidth.value * 2 +
                    lineCount * (lineHeight.value + codeLineTopPadding.value)
            )
        )
    }

    val codeFenceContainer = instructionsOf(
        borders.outline,
        backgrounds.surface,
        paddingRight { 8.dp },
        verticalScroll,
        colTemplate(48.dp, 1.fr),
        cornerRadius { 2.dp }
    )

    val lineNumberColumn = instructionsOf(
        width { 48.dp },
        backgrounds.surfaceVariant,
        paddingLeft { 10.dp },
        paddingTop { 3.dp },
        paddingBottom { 6.dp },
        borderRight(colors.lightOutline)
    )

    val lineNumberContainer = instructionsOf()

    val lineNumberText = instructionsOf(
        fontName { "Courier New" },
        fontSize { 14.sp },
        paddingTop { codeLineTopPadding },
        lineHeight { lineHeight },
        textColors.onSurfaceVariant,
        normalFont
    )

    val codeColumn = instructionsOf(
        paddingTop { containerTopPadding },
        paddingBottom { containerBottomPadding },
        paddingLeft { 12.dp },
        maxWidth,
        horizontalScroll
    )

    val codeText = instructionsOf(
        height { lineHeight + codeLineTopPadding },
        fontName { "Courier New" },
        fontSize { 14.sp },
        paddingTop { codeLineTopPadding },
        lineHeight { lineHeight }
    )

    companion object {
        var DEFAULT = CodeFenceTheme()
    }
}