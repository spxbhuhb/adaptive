package `fun`.adaptive.document.ui

import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

class DocumentTheme {

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

    val rule = instructionsOf(
        maxWidth,
        height { 14.dp },
        backgrounds.friendly,
        marginBottom { 12.dp },
        cornerRadius { 1.dp }
    )

    val bulletListIndent = 12.dp
    val numberListIndent = 16.dp

    val standaloneList = instructionsOf(
        gap { 2.dp },
        marginBottom { 12.dp }
    )

    val innerList = instructionsOf(
        gap { 2.dp }
    )

    val listBulletContainer = instructionsOf(
        paddingTop { 7.dp },
        paddingRight { 6.dp }
    )

    val listBullet = instructionsOf(
        backgroundColor(colors.onSurface),
        width { 5.dp },
        height { 5.dp },
        cornerRadius { 2.5.dp }
    )

    val listNumberContainer = instructionsOf(
        paddingRight { 6.dp }
    )

    val listNumber = instructionsOf(
        fontSize { 14.sp },
        semiBoldFont
    )

    fun listPath(item: DocListItem): String =
        item.path.joinToString(".") + "."

    val quoteDecorationWidth = 4.dp

    companion object {
        val DEFAULT = DocumentTheme()
    }

}