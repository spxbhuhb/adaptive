package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.codefence.CodeFenceTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds

class RichTextTheme {

    val h1 = instructionsOf(
        fontSize { 28.sp }
    )

    val h2 = instructionsOf(
        fontSize { 24.sp }
    )

    val h3 = instructionsOf(
        fontSize { 20.sp }
    )

    val h4 = instructionsOf(
        fontSize { 16.sp }
    )

    val h5 = instructionsOf(
        fontSize { 12.sp }
    )

    val hN = instructionsOf(
        fontSize { 12.sp }
    )

    val normal = instructionsOf(
        fontSize { 14.sp }
    )

    val bold = instructionsOf(
        semiBoldFont
    )

    val italic = instructionsOf()

    val inlineCode = instructionsOf(
        backgrounds.lightOverlay,
        paddingHorizontal { 4.dp },
        cornerRadius { 3.dp }
    )

    val paragraph = instructionsOf(
        maxWidth,
        marginBottom { 12.dp }
    )

    val innerParagraph = emptyInstructions

    val codeFence = instructionsOf(
        marginBottom { 12.dp }
    )

    val codeFenceTheme = CodeFenceTheme.DEFAULT

    companion object {
        val DEFAULT = RichTextTheme()
    }

}