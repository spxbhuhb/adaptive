package `fun`.adaptive.ui.codefence

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

class CodeFenceTheme {

    val fenceLineHeight = 20 // in dp

    val lineHeight = 17.dp
    val codeLineTopPadding = 3.dp

    /**
     * Calculate the height of the code fence. This is necessary to because we want
     * both sizing strategies to work. When height is provided, we want to use that.
     * when not, we want to size according to the lines. However, we also have to
     * scroll if the height is not enough.
     */
    fun height(fragment: AdaptiveFragment, lineCount: Int): AdaptiveInstruction {

        val uiAdapter = (fragment.adapter as? AbstractAuiAdapter<*, *>) ?: return nop

        val renderData = AuiRenderData(uiAdapter, null, fragment.instructions + codeFenceContainer)
        val columnPadding = AuiRenderData(uiAdapter, null, columnVerticalPadding)

        if (renderData.layout?.instructedHeight != null) return nop

        val surrounding = uiAdapter.toDp(renderData.surroundingVertical + columnPadding.surroundingVertical).value

        val fenceHeight = surrounding + lineCount * fenceLineHeight

        return height { fenceHeight.dp }
    }

    val columnVerticalPadding = instructionsOf(
        paddingTop { 3.dp },
        paddingBottom { 6.dp }
    )

    val codeFenceContainer = instructionsOf(
        maxWidth,
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
        borderRight(colors.lightOutline),
        columnVerticalPadding,
        paddingLeft { 10.dp }
    )

    val lineNumberContainer = instructionsOf(
        height { fenceLineHeight.dp }
    )

    val lineNumberText = instructionsOf(
        fontName { "Courier New" },
        fontSize { 14.sp },
        paddingTop { codeLineTopPadding },
        lineHeight { lineHeight },
        textColors.onSurfaceVariant,
        normalFont
    )

    val codeColumn = instructionsOf(
        maxWidth,
        horizontalScroll,
        columnVerticalPadding,
        paddingLeft { 12.dp }
    )

    val codeLineContainer = instructionsOf(
        height { fenceLineHeight.dp }
    )

    val codeText = instructionsOf(
        fontName { "Courier New" },
        fontSize { 14.sp },
        paddingTop { codeLineTopPadding },
        lineHeight { lineHeight }
    )

    companion object {
        var DEFAULT = CodeFenceTheme()
    }
}