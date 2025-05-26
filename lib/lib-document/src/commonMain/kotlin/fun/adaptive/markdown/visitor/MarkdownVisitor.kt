package `fun`.adaptive.markdown.visitor

import `fun`.adaptive.markdown.model.*

abstract class MarkdownVisitor<out R, in D> {

    abstract fun visitElement(element: MarkdownElement, data: D): R

    open fun visitHeader(header: MarkdownHeader, context: D): R =
        visitElement(header, context)

    open fun visitInline(inline: MarkdownInline, context: D) : R =
        visitElement(inline, context)

    open fun visitParagraph(paragraph: MarkdownParagraph, context: D): R =
        visitElement(paragraph, context)

    open fun visitList(list: MarkdownList, context: D): R =
         visitElement(list, context)

    open fun visitListItem(listItem: MarkdownListItem, context: D): R =
        visitElement(listItem, context)

    open fun visitCodeFence(codeFence: MarkdownCodeFence, context: D): R =
        visitElement(codeFence, context)

    open fun visitQuote(quote: MarkdownQuote, context: D): R =
        visitElement(quote, context)

    open fun visitElementGroup(group: MarkdownElementGroup, context: D): R =
        visitElement(group, context)

    open fun visitHorizontalRule(horizontalRule: MarkdownHorizontalRule, context: D): R =
        visitElement(horizontalRule, context)

}