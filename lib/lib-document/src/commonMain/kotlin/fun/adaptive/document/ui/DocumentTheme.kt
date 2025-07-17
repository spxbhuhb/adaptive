package `fun`.adaptive.document.ui

import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import kotlin.math.ceil

open class DocumentTheme(
    val baseFontSize: SPixel = 16.sp,
    val textColor: Color = colors.onSurface,
    val monoSpaceFont: String = "Courier New",
    val monoSpaceFontSize: SPixel = 15.sp,
) {

    companion object {
        var default = DocumentTheme()

        var hint = DocumentTheme(
            baseFontSize = 12.sp,
            textColor = colors.onSurface,
            monoSpaceFontSize = 11.sp
        )
    }

    // without ceil Firefox canvas measures text differently than DOM and `...` will appear

    fun SPixel.scale(ratio: Double) = (ceil(this.value * ratio)).sp

    fun scaledDp(ratio: Double) = (ceil(baseFontSize.value * ratio)).dp

    /**
     * The gap between blocks of content such as paragraphs.
     */
    open val blockGap = gap { 24.dp }


    open var h1 = instructionsOf(
        fontSize { baseFontSize.scale(1.8) },
        fontWeight { 500 },
        textColor(textColor),
    )

    open var h2 = instructionsOf(
        fontSize { baseFontSize.scale(1.6) },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h3 = instructionsOf(
        fontSize { baseFontSize.scale(1.4) },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h4 = instructionsOf(
        fontSize { baseFontSize.scale(1.2) },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var h5 = instructionsOf(
        fontSize { baseFontSize },
        fontWeight { 500 },
        textColor(textColor)
    )

    open var hN = instructionsOf(
        fontSize { baseFontSize.scale(0.8) },
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
        fontSize { monoSpaceFontSize },
        textColor(textColor),
        backgrounds.surfaceVariant,
        paddingHorizontal { 6.dp },
        cornerRadius { 3.dp },
        borders.outline,
        marginTop { (if (baseFontSize.value > 12) 1 else 0).dp }
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

    open var bulletListIndent = scaledDp(1.1)
    open var numberListIndent = scaledDp(1.3)

    open var standaloneList = instructionsOf(
        gap { 4.dp } .. paddingLeft { scaledDp(1.0) }
    )

    open var innerList = instructionsOf(
        gap { 4.dp }
    )

    open var listBulletContainer = instructionsOf(
        padding(scaledDp(9.0 / 16.0), 10.dp, 0.dp, 8.dp)
    )

    open var listBullet = instructionsOf(
        backgroundColor(textColor),
        size(scaledDp(6.0 / 16.0)),
        cornerRadius { scaledDp(3.0 / 16.0) }
    )

    open var listNumberContainer = instructionsOf(
        padding(0.dp, 10.dp, 0.dp, 12.dp)
    )

    open var listNumber = instructionsOf(
        fontSize { baseFontSize },
        semiBoldFont,
        textColor(textColor),
    )

    open fun listPath(item: DocListItem): String =
        item.path.joinToString(".") + "."

    open val quote = instructionsOf()

    open val quoteDecorationWidth = 4.dp

    open val blockImage = instructionsOf()

    open val blockFragment = instructionsOf()

}