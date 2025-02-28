package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.model.MarkdownCodeFenceAstEntry
import `fun`.adaptive.markdown.model.MarkdownHeaderAstEntry
import `fun`.adaptive.markdown.model.MarkdownHorizontalRuleAstEntry
import `fun`.adaptive.markdown.model.MarkdownInlineAstEntry
import `fun`.adaptive.markdown.model.MarkdownListAstEntry
import `fun`.adaptive.markdown.model.MarkdownParagraphAstEntry
import `fun`.adaptive.markdown.model.MarkdownQuoteAstEntry

interface MarkdownAstTransform<C, R> {
    fun visit(header: MarkdownHeaderAstEntry, context: C): R
    fun visit(inline: MarkdownInlineAstEntry, context: C): R
    fun visit(paragraph: MarkdownParagraphAstEntry, context: C): R
    fun visit(list: MarkdownListAstEntry, context: C): R
    fun visit(codeFence: MarkdownCodeFenceAstEntry, context: C): R
    fun visit(quote: MarkdownQuoteAstEntry, context: C): R
    fun visit(horizontalRule: MarkdownHorizontalRuleAstEntry, context: C): R
}