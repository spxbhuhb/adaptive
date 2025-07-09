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
import `fun`.adaptive.ui.theme.textColors

open class DocumentTheme(
    val baseFontSize: SPixel = 16.sp,
    val textColor: Color = colors.onSurface,
    val monoSpaceFont: String = "Courier New"
) {

    /**
     * The gap between blocks of content such as paragraphs.
     */
    open val blockGap = gap { 24.dp }

    open var h1 = instructionsOf(
        fontSize { 28.sp },
        fontWeight { 500 },
        textColor(textColor),
    )

    open var h2 = instructionsOf(
        fontSize { 24.sp },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h3 = instructionsOf(
        fontSize { 20.sp },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h4 = instructionsOf(
        fontSize { 16.sp },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h5 = instructionsOf(
        fontSize { 12.sp },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var hN = instructionsOf(
        fontSize { 12.sp },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var normal = instructionsOf(
        fontSize { baseFontSize },
        textColor(textColor)
    )

    open var bold = instructionsOf(
        semiBoldFont
    )

    open var italic = instructionsOf()

    open var inlineCode = instructionsOf(
        fontName { monoSpaceFont },
        fontSize { baseFontSize },
        textColor(textColor),
        backgrounds.lightOverlay,
        paddingHorizontal { 4.dp },
        cornerRadius { 3.dp }
    )

    open var link = instructionsOf(
        fontSize { baseFontSize },
        textColors.primary
    )

    open var paragraph = instructionsOf(
        maxWidth
    )

    open var innerParagraph = emptyInstructions

    open var codeFence = instructionsOf(

    )

    open var rule = instructionsOf(
        maxWidth,
        height { 2.dp },
        backgrounds.friendly,
        cornerRadius { 1.dp }
    )

    open var bulletListIndent = 12.dp
    open var numberListIndent = 16.dp

    open var standaloneList = instructionsOf(
        gap { 2.dp }
    )

    open var innerList = instructionsOf(
        gap { 2.dp }
    )

    open var listBulletContainer = instructionsOf(
        paddingTop { 7.dp },
        paddingRight { 6.dp }
    )

    open var listBullet = instructionsOf(
        backgroundColor(textColor),
        width { 5.dp },
        height { 5.dp },
        cornerRadius { 2.5.dp }
    )

    open var listNumberContainer = instructionsOf(
        paddingRight { 6.dp }
    )

    open var listNumber = instructionsOf(
        fontSize { 14.sp },
        semiBoldFont,
        textColor(textColor),
    )

    open fun listPath(item: DocListItem): String =
        item.path.joinToString(".") + "."

    open val quote = instructionsOf()

    open val quoteDecorationWidth = 4.dp

    open val blockImage = instructionsOf()

    open val blockFragment = instructionsOf()

    companion object {
        var default = DocumentTheme()
        var hint = DocumentTheme(baseFontSize = 12.sp, textColor = colors.onSurface)
    }

}