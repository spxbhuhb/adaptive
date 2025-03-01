package `fun`.adaptive.markdown.transform

import `fun`.adaptive.document.model.*
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.parse.parse
import `fun`.adaptive.markdown.parse.tokenize

class MarkdownToDocTransform(
    val ast: List<MarkdownAstEntry>,
    val theme: DocumentTheme = DocumentTheme.DEFAULT,
) : MarkdownAstTransform<Int, DocBlockElement?> {

    constructor(source: String) : this(parse(tokenize(source)))

    companion object {
        const val BOLD = 0x1
        const val ITALIC = 0x2
        const val CODE = 0x4
        const val LINK = 0x8
        const val REFERENCE_LINK = 0x10
        const val REFERENCE_DEF = 0x20
        const val CODE_FENCE = 0x40
        const val QUOTE = 0x80
        const val HEADER = 0x700

        private val inlineLinkRegex = "\\[([^\\[]+)\\]\\(([^)]+)\\)".toRegex()
    }

    val usedStyles = mutableListOf<Int>()

    val Int.styleIndex: Int
        get() {
            val index = usedStyles.indexOf(this)
            if (index != - 1) return index
            usedStyles.add(this)
            return (usedStyles.size - 1)
        }

    var inlineStack = mutableListOf<DocInlineElement>()

    fun consumeInlineStack(): List<DocInlineElement> {
        val items = inlineStack
        inlineStack = mutableListOf()
        return items
    }

    fun transform(): DocDocument {
        val elements = ast.mapNotNull { it.accept(this, 0) }
        val styles = usedStyles.mapIndexed { index, mask -> buildStyle(index, mask) }

        return DocDocument(-1, elements, styles)
    }

    override fun visit(header: MarkdownHeaderAstEntry, styleMask: Int): DocHeader {
        val mask = styleMask or (header.level shl 8)

        // fills the inline stack
        header.children.forEach { it.accept(this, mask) }

        return DocHeader(styleMask.styleIndex, consumeInlineStack(), header.level)
    }

    @Suppress("SameReturnValue")
    override fun visit(inline: MarkdownInlineAstEntry, styleMask: Int): DocBlockElement? {

        if (inline.text.isEmpty()) return null

        var mask = styleMask

        if (inline.bold) mask = mask or BOLD
        if (inline.italic) mask = mask or ITALIC
        if (inline.code) mask = mask or CODE

        when {
            inline.inlineLink -> {
                val match = inlineLinkRegex.matchEntire(inline.text) ?: return null
                inlineStack += DocLink(mask.styleIndex, match.groupValues[1], match.groupValues[2])
            }

            else -> {
                inlineStack += DocText(mask.styleIndex, inline.text)
            }
        }

        return null
    }

    override fun visit(paragraph: MarkdownParagraphAstEntry, styleMask: Int): DocParagraph {

        // fills the inline stack
        paragraph.children.forEach { it.accept(this, styleMask) }

        return DocParagraph(-1, consumeInlineStack(), standalone = true)
    }

    override fun visit(list: MarkdownListAstEntry, styleMask: Int): DocList {

        // fills the inline stack
        list.children.forEach { it.accept(this, styleMask) }

        DocList(
            - 1,
            listOf(DocParagraph(-1, consumeInlineStack(), standalone = false)),
            list.bullet,
            list.level
        ).also { return it }
    }

    override fun visit(codeFence: MarkdownCodeFenceAstEntry, styleMask: Int): DocCodeFence? =
        DocCodeFence(-1, codeFence.content, codeFence.language)

    override fun visit(quote: MarkdownQuoteAstEntry, styleMask: Int): DocQuote =
        DocQuote(-1, quote.children.mapNotNull { it.accept(this, styleMask) })

    override fun visit(horizontalRule: MarkdownHorizontalRuleAstEntry, styleMask: Int): DocRule =
        DocRule()

    fun buildStyle(index: Int, mask: Int): DocStyle {

        val out = mutableListOf<AdaptiveInstruction>()

        if (mask and BOLD != 0) theme.bold.toMutableList(out)
        if (mask and ITALIC != 0) theme.italic.toMutableList(out)
        if (mask and CODE != 0) theme.inlineCode.toMutableList(out)

        val header = (mask and HEADER) shr 8

        when (header) {
            0 -> theme.normal.toMutableList(out)
            1 -> theme.h1.toMutableList(out)
            2 -> theme.h2.toMutableList(out)
            3 -> theme.h3.toMutableList(out)
            4 -> theme.h4.toMutableList(out)
            5 -> theme.h5.toMutableList(out)
            else -> theme.hN.toMutableList(out)
        }

        return DocStyle(index, AdaptiveInstructionGroup(out))
    }
}