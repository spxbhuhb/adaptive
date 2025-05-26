package `fun`.adaptive.markdown.visitor

import `fun`.adaptive.markdown.model.*

abstract class MarkdownTransformer<in D> : MarkdownVisitor<MarkdownElement, D>() {

    override fun visitElement(element: MarkdownElement, data: D) : MarkdownElement {
        element.transformChildren(this, data)
        return element
    }

    override fun visitHeader(header: MarkdownHeader, context: D): MarkdownElement =
        visitElement(header, context)

    override fun visitInline(inline: MarkdownInline, context: D) : MarkdownElement =
        visitElement(inline, context)

    override fun visitParagraph(paragraph: MarkdownParagraph, context: D): MarkdownElement =
        visitElement(paragraph, context)

    override fun visitList(list: MarkdownList, context: D): MarkdownElement =
        visitElement(list, context)

    override fun visitListItem(listItem: MarkdownListItem, context: D): MarkdownElement =
        visitElement(listItem, context)

    override fun visitCodeFence(codeFence: MarkdownCodeFence, context: D): MarkdownElement =
        visitElement(codeFence, context)

    override fun visitQuote(quote: MarkdownQuote, context: D): MarkdownElement =
        visitElement(quote, context)

    override fun visitElementGroup(group: MarkdownElementGroup, context: D): MarkdownElement =
        visitElement(group, context)

    override fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: D): MarkdownElement =
        visitElement(horizontalRule, context)
}