package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.visitor.MarkdownVisitor
import `fun`.adaptive.markdown.model.*

class MarkdownToMarkdownVisitor : MarkdownVisitor<Unit, StringBuilder>() {

    var level = 0
    var noQuote = false

    override fun visitElement(element: MarkdownElement, data: StringBuilder) {
        element.acceptChildren(this, data)
    }

    override fun visitHeader(header: MarkdownHeader, context: StringBuilder) {
        addQuote(context)
        context.append("#".repeat(header.level)).append(" ")
        header.children.forEach { it.accept(this, context) }
        if (level == 0) {
            context.append("\n\n")
        } else {
            context.append("\n")
        }
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
        addQuote(context)
        paragraph.children.forEach { it.accept(this, context) }
        if (paragraph.closed) {
            if (level == 0) {
                context.append("\n\n")
            } else {
                context.append("\n")
            }
        } else {
            context.append("\n")
        }
    }

    override fun visitList(list: MarkdownList, context: StringBuilder) {
        list.items.forEach { it.accept(this, context) }
        if (list.level == 1) {
            if (level == 0) {
                context.append("\n")
            }
        }
    }

    override fun visitListItem(listItem: MarkdownListItem, context: StringBuilder) {
        addQuote(context)
        context.append("    ".repeat(listItem.level - 1))
        context.append(if (listItem.bullet) "* " else "1. ")

        noQuote = true
        listItem.content.accept(this, context)
        noQuote = false

        listItem.subList?.accept(this, context)
    }

    override fun visitCodeFence(codeFence: MarkdownCodeFence, context: StringBuilder) {
        addQuote(context)

        context.append("```")
        if (codeFence.language.isNullOrBlank().not()) {
            context.append(codeFence.language)
        }
        context.append("\n")
        codeFence.content.lines().forEach {
            addQuote(context)
            context.append(it).append("\n")
        }
        context.append("```")
        if (level == 0) {
            context.append("\n\n")
        } else {
            context.append("\n")
        }
    }

    override fun visitQuote(quote: MarkdownQuote, context: StringBuilder) {
        level++
        quote.children.forEach { child ->
            child.accept(this, context)
        }
        level--
        if (level == 0) context.append("\n")
    }

    fun addQuote(context: StringBuilder) {
        if (noQuote) return
        for (i in 0 until level) {
            context.append("> ")
        }
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
