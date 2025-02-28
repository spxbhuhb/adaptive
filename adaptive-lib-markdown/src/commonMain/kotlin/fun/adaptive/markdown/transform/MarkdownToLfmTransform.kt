package `fun`.adaptive.markdown.transform

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.parse.parse
import `fun`.adaptive.markdown.parse.tokenize
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

class MarkdownToLfmTransform(
    val ast: List<MarkdownAstEntry>,
    val theme: MarkdownTheme = MarkdownTheme.DEFAULT
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

    fun transform(): LfmFragment {
        val descendants = ast.mapNotNull { it.accept(this, 0) }

        val instructionSets = usedStyles.map { buildStyle(it) }

        return LfmFragment(
            emptyList(),
            emptyList(),
            descendants.map { it.toLfmDescendant(instructionSets) }
        )
    }

    override fun visit(header: MarkdownHeaderAstEntry, styleMask: Int): TransformEntry {
        val mask = styleMask or (header.level shl 8)

        header.children.forEach { it.accept(this, mask) }

        return TransformEntry(nextIndex, "aui:paragraph", consumeInlineStack())
    }

    override fun visit(inline: MarkdownInlineAstEntry, styleMask: Int): TransformEntry? {
        var mask = styleMask

        if (inline.bold) mask = mask or BOLD
        if (inline.italic) mask = mask or ITALIC
        if (inline.code) mask = mask or CODE

        when {
            inline.inlineLink -> return null
            else -> inlineStack += TextParagraphItem(inline.text, mask.styleIndex)
        }

        return null
    }

    override fun visit(paragraph: MarkdownParagraphAstEntry, styleMask: Int): TransformEntry {

        paragraph.children.forEach { it.accept(this, styleMask) }

        return TransformEntry(nextIndex, "aui:paragraph", consumeInlineStack())

    }

    override fun visit(list: MarkdownListAstEntry, styleMask: Int): TransformEntry {
        return TransformEntry(
            nextIndex,
            "aui:listWrap",
            children = list.children.mapNotNull { it.accept(this, styleMask) }
        )
    }

    override fun visit(codeFence: MarkdownCodeFenceAstEntry, styleMask: Int): TransformEntry? {
        return null
        // sb.append(codeFence.content)
    }

    override fun visit(quote: MarkdownQuoteAstEntry, styleMask: Int): TransformEntry {
        return TransformEntry(
            nextIndex,
            "aui:quoteWrap",
            children = quote.children.mapNotNull { it.accept(this, styleMask) }
        )
    }

    override fun visit(horizontalRule: MarkdownHorizontalRuleAstEntry, styleMask: Int): TransformEntry {
        return TransformEntry(nextIndex, "aui:box")
    }

    fun buildStyle(mask: Int): AdaptiveInstructionGroup {

        val out = mutableListOf<AdaptiveInstruction>()

        if (mask and BOLD != 0) theme.bold.toMutableList(out)
        if (mask and ITALIC != 0) theme.italic.toMutableList(out)
        if (mask and CODE != 0) theme.code.toMutableList(out)

        val header = (mask and HEADER) shr 8

        when (header) {
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