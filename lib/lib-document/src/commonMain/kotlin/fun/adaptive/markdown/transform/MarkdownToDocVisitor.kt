package `fun`.adaptive.markdown.transform

import `fun`.adaptive.document.model.*
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.markdown.visitor.MarkdownVisitor
import `fun`.adaptive.markdown.compiler.parseInternal
import `fun`.adaptive.markdown.compiler.tokenizeInternal
import `fun`.adaptive.markdown.model.*

class MarkdownToDocVisitor(
    val ast: List<MarkdownElement>,
    val theme: DocumentTheme = DocumentTheme.default,
) : MarkdownVisitor<DocBlockElement?, Int>() {

    constructor(source: String) : this(parseInternal(tokenizeInternal(source)))

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

    var level: Int = 0

    var inlineStack = mutableListOf<DocInlineElement>()

    fun consumeInlineStack(): List<DocInlineElement> {
        val items = inlineStack
        inlineStack = mutableListOf()
        return items
    }

    fun transform(): DocDocument {
        val elements = ast.mapNotNull { it.accept(this, 0) }
        val styles = usedStyles.mapIndexed { index, mask -> buildStyle(index, mask) }

        return DocDocument(- 1, elements, styles)
    }

    override fun visitElement(element: MarkdownElement, data: Int): DocBlockElement? {
        // all child specific stuff is handled by type specific visitors
        error("Should not be called")
    }

    override fun visitHeader(header: MarkdownHeader, context: Int): DocHeader {
        val mask = context or (header.level shl 8)

        // fills the inline stack
        header.children.forEach { it.accept(this, mask) }

        return DocHeader(context.styleIndex, consumeInlineStack(), header.level)
    }

    @Suppress("SameReturnValue")
    override fun visitInline(inline: MarkdownInline, context: Int): DocBlockElement? {

        if (inline.text.isEmpty()) return null

        var mask = context

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

    override fun visitParagraph(paragraph: MarkdownParagraph, context: Int): DocBlockElement {

        if (paragraph.children.size == 1) {
            val first = paragraph.children.first()
            maybeBlock(first)?.let { return it }
        }

        // fills the inline stack
        paragraph.children.forEach { it.accept(this, context) }

        return DocParagraph(- 1, consumeInlineStack(), standalone = (level == 0))
    }

    private fun maybeBlock(element: MarkdownElement?): DocBlockElement? {
        (element as? MarkdownInline) ?: return null
        if (! element.inlineLink && ! element.imageLink) return null

        val match = inlineLinkRegex.matchEntire(element.text) !! // should not be null at this point
        val text = match.groupValues[1]
        val url = match.groupValues[2]

        when {
            element.imageLink -> DocBlockImage(- 1, text, url)
            url.startsWith("actualize://") -> DocBlockFragment(- 1, text, url)
            else -> null
        }.also {
            return it
        }
    }

    val listPath = mutableListOf<Int>()

    override fun visitList(list: MarkdownList, context: Int): DocBlockElement {

        level ++
        listPath += 0
        val pathIndex = listPath.lastIndex

        val children = list.items.map {
            listPath[pathIndex] = listPath[pathIndex] + 1
            it.accept(this, context)
        }

        listPath.removeLast()
        level --

        @Suppress("UNCHECKED_CAST") // Markdown AST should guarantee that only list items are in the list
        return DocList(- 1, children as List<DocListItem>, level == 0)
    }

    override fun visitListItem(listItem: MarkdownListItem, context: Int): DocBlockElement? {
        return DocListItem(
            - 1,
            listItem.content.accept(this, context) as DocBlockElement,
            listItem.subList?.accept(this, context) as? DocList,
            listPath.toList(),
            listItem.bullet
        )
    }

    override fun visitCodeFence(codeFence: MarkdownCodeFence, context: Int): DocCodeFence? =
        DocCodeFence(- 1, codeFence.content, codeFence.language)

    override fun visitQuote(quote: MarkdownQuote, context: Int): DocQuote =
        // quote content should be treated as standalone to have proper separation
        DocQuote(- 1, quote.children.mapNotNull { it.accept(this, context) })

    override fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: Int): DocRule =
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