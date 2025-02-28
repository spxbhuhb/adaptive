package `fun`.adaptive.markdown.transform

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds


class MarkdownTheme {

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

    val bold = instructionsOf(
        semiBoldFont
    )

    val italic = instructionsOf()

    val code = instructionsOf(
        backgrounds.lightOverlay,
        paddingHorizontal { 4.dp },
        cornerRadius { 3.dp }
    )

    companion object {
        val DEFAULT = MarkdownTheme()
    }
}