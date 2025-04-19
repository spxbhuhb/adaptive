package `fun`.adaptive.document.ui

import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

class DocumentTheme(
    val baseFontSize : SPixel = 14.sp,
    val textColor : Color = colors.onSurface
) {

    /**
     * The gap between blocks of content such as paragraphs.
     */
    val blockGap = gap { 12.dp }

    val h1 = instructionsOf(
        fontSize { 28.sp },
        textColor(textColor)
    )

    val h2 = instructionsOf(
        fontSize { 24.sp },
        textColor(textColor)
    )

    val h3 = instructionsOf(
        fontSize { 20.sp },
        textColor(textColor)
    )

    val h4 = instructionsOf(
        fontSize { 16.sp },
        textColor(textColor)
    )

    val h5 = instructionsOf(
        fontSize { 12.sp },
        textColor(textColor)
    )

    val hN = instructionsOf(
        fontSize { 12.sp },
        textColor(textColor)
    )

    val normal = instructionsOf(
        fontSize { baseFontSize },
        textColor(textColor)
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
        maxWidth
    )

    val innerParagraph = emptyInstructions

    val codeFence = instructionsOf(

    )

    val rule = instructionsOf(
        maxWidth,
        height { 2.dp },
        backgrounds.friendly,
        cornerRadius { 1.dp }
    )

    val bulletListIndent = 12.dp
    val numberListIndent = 16.dp

    val standaloneList = instructionsOf(
        gap { 2.dp }
    )

    val innerList = instructionsOf(
        gap { 2.dp }
    )

    val listBulletContainer = instructionsOf(
        paddingTop { 7.dp },
        paddingRight { 6.dp }
    )

    val listBullet = instructionsOf(
        backgroundColor(textColor),
        width { 5.dp },
        height { 5.dp },
        cornerRadius { 2.5.dp }
    )

    val listNumberContainer = instructionsOf(
        paddingRight { 6.dp }
    )

    val listNumber = instructionsOf(
        fontSize { 14.sp },
        semiBoldFont,
        textColor(textColor),
    )

    fun listPath(item: DocListItem): String =
        item.path.joinToString(".") + "."

    val quote = instructionsOf()

    val quoteDecorationWidth = 4.dp

    val blockImage = instructionsOf()

    val blockFragment = instructionsOf()

    companion object {
        val default = DocumentTheme()
        val hint = DocumentTheme(baseFontSize = 12.sp, textColor = colors.onSurfaceVariant)
    }

}