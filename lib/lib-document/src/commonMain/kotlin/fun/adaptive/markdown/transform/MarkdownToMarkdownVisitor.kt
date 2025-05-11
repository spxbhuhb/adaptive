package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.visitor.MarkdownVisitor
import `fun`.adaptive.markdown.model.*

class MarkdownToMarkdownVisitor : MarkdownVisitor<Unit, StringBuilder>() {

    var level = 0

    override fun visitElement(element: MarkdownElement, data: StringBuilder) {
        element.acceptChildren(this, data)
    }

    override fun visitHeader(header: MarkdownHeader, context: StringBuilder) {
        context.append("#".repeat(header.level)).append(" ")
        header.children.forEach { it.accept(this, context) }
        context.append("\n\n")
    }

    override fun visitInline(inline: MarkdownInline, context: StringBuilder) {
        var text = inline.text

        if (inline.code) {
            text = "`$text`"
        }
        if (inline.bold) {
            text = "**$text**"
        }
        if (inline.italic) {
            text = "_${text}_"
        }

        context.append(text)
    }

    override fun visitParagraph(paragraph: MarkdownParagraph, context: StringBuilder) {
        paragraph.children.forEach { it.accept(this, context) }
        if (paragraph.closed) {
            context.append("\n\n")
        }
    }

    override fun visitList(list: MarkdownList, context: StringBuilder) {
        list.items.forEach { it.accept(this, context) }
        if (list.level == 1) {
            context.append("\n")
        }
    }

    override fun visitListItem(listItem: MarkdownListItem, context: StringBuilder) {
        context.append("    ".repeat(listItem.level - 1))
        context.append(if (listItem.bullet) "* " else "1. ")
        listItem.content.accept(this, context)
        context.append("\n")
        listItem.subList?.accept(this, context)
    }

    override fun visitCodeFence(codeFence: MarkdownCodeFence, context: StringBuilder) {
        context.append("```")
        if (codeFence.language.isNullOrBlank().not()) {
            context.append(codeFence.language)
        }
        context.append("\n")
        context.append(codeFence.content)
        context.append("\n```\n\n")
    }

    override fun visitQuote(quote: MarkdownQuote, context: StringBuilder) {
        level++
        quote.children.forEach { child ->
            context.append("> ")
            child.accept(this, context)
            if (level == 1) context.append("\n")
        }
        level--
        if (level == 0) context.append("\n")
    }

    override fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: StringBuilder) {
        context.append("---\n\n")
    }

    companion object {
        fun List<MarkdownElement>.toMarkdown(): String {
            val sb = StringBuilder()
            MarkdownToMarkdownVisitor().apply {
                forEach { it.accept(this, sb) }
            }
            return sb.toString().trimEnd()
        }
    }
}