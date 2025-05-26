package `fun`.adaptive.markdown.visitor

import `fun`.adaptive.markdown.model.*

abstract class MarkdownTransformerVoid : MarkdownTransformer<Nothing?>() {

    open fun visitElement(element: MarkdownElement): MarkdownElement =
        visitElement(element, null)

    final override fun visitElement(element: MarkdownElement, data: Nothing?): MarkdownElement {
        element.transformChildren(this, data)
        return element
    }

    open fun visitHeader(header: MarkdownHeader): MarkdownElement =
        visitElement(header, null)

    final override fun visitHeader(header: MarkdownHeader, context: Nothing?): MarkdownElement =
        visitHeader(header)

    open fun visitInline(inline: MarkdownInline): MarkdownElement =
        visitElement(inline, null)

    final override fun visitInline(inline: MarkdownInline, context: Nothing?): MarkdownElement =
        visitInline(inline)

    open fun visitParagraph(paragraph: MarkdownParagraph): MarkdownElement =
        visitElement(paragraph, null)

    final override fun visitParagraph(paragraph: MarkdownParagraph, context: Nothing?): MarkdownElement =
        visitParagraph(paragraph)

    open fun visitList(list: MarkdownList): MarkdownElement =
        visitElement(list, null)

    final override fun visitList(list: MarkdownList, context: Nothing?): MarkdownElement =
        visitList(list)

    open fun visitListItem(listItem: MarkdownListItem): MarkdownElement =
        visitElement(listItem, null)

    final override fun visitListItem(listItem: MarkdownListItem, context: Nothing?): MarkdownElement =
        visitListItem(listItem)

    open fun visitCodeFence(codeFence: MarkdownCodeFence): MarkdownElement =
        visitElement(codeFence, null)

    final override fun visitCodeFence(codeFence: MarkdownCodeFence, context: Nothing?): MarkdownElement =
        visitCodeFence(codeFence)

    open fun visitQuote(quote: MarkdownQuote): MarkdownElement =
        visitElement(quote, null)

    final override fun visitQuote(quote: MarkdownQuote, context: Nothing?): MarkdownElement =
        visitQuote(quote)

    open fun visitElementGroup(group: MarkdownElementGroup): MarkdownElement =
        visitElement(group, null)

    final override fun visitElementGroup(group: MarkdownElementGroup, context: Nothing?): MarkdownElement =
        visitElementGroup(group)

    open fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule): MarkdownElement =
        visitElement(horizontalRule, null)

    final override fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: Nothing?): MarkdownElement =
        visitHorizontalRule(horizontalRule)
}