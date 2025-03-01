package `fun`.adaptive.markdown.transform

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.parse.parse
import `fun`.adaptive.markdown.parse.tokenize
import `fun`.adaptive.markdown.transform.entry.CodeFenceEntry
import `fun`.adaptive.markdown.transform.entry.HorizontalRuleEntry
import `fun`.adaptive.markdown.transform.entry.ListEntry
import `fun`.adaptive.markdown.transform.entry.ParagraphEntry
import `fun`.adaptive.markdown.transform.entry.QuoteEntry
import `fun`.adaptive.markdown.transform.entry.TransformEntry
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.ui.richtext.RichText
import `fun`.adaptive.utility.isWhiteSpace
import `fun`.adaptive.utility.words

class MarkdownToLfmTransform(
    val ast: List<MarkdownAstEntry>,
    val config: MarkdownTransformConfig = MarkdownTransformConfig()
) : MarkdownAstTransform<Int, TransformEntry?> {

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

    var nextIndex: Int = 0
        get() = field ++

    val usedStyles = mutableListOf<Int>()

    val Int.styleIndex: Int
        get() {
            val index = usedStyles.indexOf(this)
            if (index != - 1) return index
            usedStyles.add(this)
            return (usedStyles.size - 1)
        }

    var inlineStack = mutableListOf<ParagraphItem>()

    fun consumeInlineStack(): List<ParagraphItem> {
        val items = inlineStack
        inlineStack = mutableListOf()
        return items
    }

    fun transform(): RichText {
        val descendants = ast.mapNotNull { it.accept(this, 0) }

        val instructionSets = usedStyles.map { buildStyle(it) }

        RichText(
            instructionSets,
            LfmFragment(
                emptyList(),
                emptyList(),
                descendants.map { it.toLfmDescendant(config, instructionSets) }
            ),
            config.theme
        ).also { return it }
    }

    override fun visit(header: MarkdownHeaderAstEntry, styleMask: Int): TransformEntry {
        val mask = styleMask or (header.level shl 8)

        header.children.forEach { it.accept(this, mask) }

        return ParagraphEntry(nextIndex, config.paragraph, consumeInlineStack())
    }

    @Suppress("SameReturnValue")
    override fun visit(inline: MarkdownInlineAstEntry, styleMask: Int): TransformEntry? {

        if (inline.text.isEmpty()) return null

        var mask = styleMask

        if (inline.bold) mask = mask or BOLD
        if (inline.italic) mask = mask or ITALIC
        if (inline.code) mask = mask or CODE

        when {
            inline.inlineLink -> {
                val match = inlineLinkRegex.matchEntire(inline.text) ?: return null
                inlineStack += LinkParagraphItem(match.groupValues[1], match.groupValues[2], mask.styleIndex)
            }

            else -> {
                if (inline.text.first().isWhiteSpace) {
                    inlineStack += TextParagraphItem(" ", mask.styleIndex)
                }
                for (word in inline.text.words()) {
                    inlineStack += TextParagraphItem(word, mask.styleIndex)
                    inlineStack += TextParagraphItem(" ", mask.styleIndex)
                }
                if (! inline.text.last().isWhiteSpace) {
                    inlineStack.removeLast()
                }
            }
        }

        return null
    }

    override fun visit(paragraph: MarkdownParagraphAstEntry, styleMask: Int): TransformEntry {

        paragraph.children.forEach { it.accept(this, styleMask) }

        return ParagraphEntry(nextIndex, config.paragraph, consumeInlineStack())

    }

    override fun visit(list: MarkdownListAstEntry, styleMask: Int): TransformEntry =
        ListEntry(nextIndex, config.list, list.children.mapNotNull { it.accept(this, styleMask) })

    override fun visit(codeFence: MarkdownCodeFenceAstEntry, styleMask: Int): TransformEntry? =
        CodeFenceEntry(nextIndex, config.codeFence, codeFence.content, codeFence.language)

    override fun visit(quote: MarkdownQuoteAstEntry, styleMask: Int): TransformEntry =
        QuoteEntry(nextIndex, config.quote, quote.children.mapNotNull { it.accept(this, styleMask) })

    override fun visit(horizontalRule: MarkdownHorizontalRuleAstEntry, styleMask: Int): TransformEntry =
        HorizontalRuleEntry(nextIndex, config.horizontalRule)

    fun buildStyle(mask: Int): AdaptiveInstructionGroup {

        val theme = config.theme
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

        return AdaptiveInstructionGroup(out)
    }
}