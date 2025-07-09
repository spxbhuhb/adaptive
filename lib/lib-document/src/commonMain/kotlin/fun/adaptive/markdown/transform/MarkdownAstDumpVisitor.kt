package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.visitor.MarkdownVisitor
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.compiler.parseInternal
import `fun`.adaptive.markdown.compiler.tokenizeInternal

/**
 * Dumps a Markdown AST for inspection.
 */
class MarkdownAstDumpVisitor() : MarkdownVisitor<Unit, MutableList<String>>() {

    var level = 0

    val indent
        get() = "    ".repeat(level).dropLast(1)

    override fun visitElement(element: MarkdownElement, data: MutableList<String>) {
        element.acceptChildren(this, data)
    }

    override fun visitHeader(header: MarkdownHeader, context: MutableList<String>) {
        context += "${indent}HEADER  level=$level"
        level++
        header.children.forEach { it.accept(this, context) }
        level--
    }

    override fun visitInline(inline: MarkdownInline, context: MutableList<String>) {
        context += "${indent}INLINE  bold=${inline.bold}  italic=${inline.italic}  code=${inline.code}  link=${inline.inlineLink}  refLink=${inline.referenceLink}  refDef=${inline.referenceDef}"
        context += "${indent}    text: ${inline.text}"
    }

    override fun visitParagraph(paragraph: MarkdownParagraph, context: MutableList<String>) {
        context += "${indent}PARAGRAPH  closed=${paragraph.closed}"
        level++
        paragraph.children.forEach { it.accept(this, context) }
        level--
    }

    override fun visitList(list: MarkdownList, context: MutableList<String>) {
        context += "${indent}LIST  bullet=${list.bullet}  level=${list.level}"
        level++
        list.items.forEach { it.accept(this, context) }
        level--
    }

    override fun visitListItem(listItem: MarkdownListItem, context: MutableList<String>) {
        context += "${indent}LIST ITEM  bullet=${listItem.bullet}  level=${listItem.level}"
        level++
        super.visitListItem(listItem, context)
        level--
    }

    override fun visitCodeFence(codeFence: MarkdownCodeFence, context: MutableList<String>) {
        context += "${indent}CODE FENCE  language=${codeFence.language}"
        context += "${indent}    text: ${codeFence.content}"
    }

    override fun visitQuote(quote: MarkdownQuote, context: MutableList<String>) {
        context += "${indent}QUOTE"
        level++
        quote.children.forEach { it.accept(this, context) }
        level--
    }

    override fun visitElementGroup(group: MarkdownElementGroup, context: MutableList<String>) {
        context += "${indent}GROUP"
        level++
        group.children.forEach { it.accept(this, context) }
        level--
    }

    override fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: MutableList<String>) {
        context += "${indent}HORIZONTAL RULE"
    }

    companion object {

        fun String.dumpMarkdown() = parseInternal(tokenizeInternal(this)).dump()

        fun List<MarkdownElement>.dump() : String {
            val dump = mutableListOf<String>()
            MarkdownAstDumpVisitor().apply {
                forEach { it.accept(this, dump) }
            }
            return dump.joinToString("\n")
        }

    }
}