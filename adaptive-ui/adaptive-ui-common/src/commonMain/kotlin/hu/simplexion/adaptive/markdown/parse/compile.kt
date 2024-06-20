/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown.parse

fun toAST(tokens: List<MarkdownToken>): List<MarkdownAstEntry> {

    val context = CompileContext(tokens)
    var index = 0
    val end = tokens.size

    while (index < end) {
        val token = tokens[index]

        index = when (token.type) {
            MarkdownTokenType.Spaces -> context.spaces(token, index)
            MarkdownTokenType.Text -> context.text(index)
            MarkdownTokenType.NewLine -> context.newLine(index)
            MarkdownTokenType.Header -> context.header(token, index)
            MarkdownTokenType.BulletList -> context.list(token, index)
            MarkdownTokenType.NumberedList -> context.list(token, index)
            MarkdownTokenType.Quote -> context.quote(index)
            MarkdownTokenType.CodeLanguage -> context.codeLanguage(token, index)
            MarkdownTokenType.CodeFence -> context.codeFence(token, index)
            MarkdownTokenType.Asterisks -> context.maybeRule(token, index)
            MarkdownTokenType.Underscores -> context.maybeRule(token, index)
            MarkdownTokenType.Hyphens -> context.maybeRule(token, index)
            MarkdownTokenType.CodeSpan -> context.text(index)
            MarkdownTokenType.InlineLink -> context.text(index)
            MarkdownTokenType.ReferenceLink -> context.text(index)
            MarkdownTokenType.ReferenceDef -> context.text(index)
        }
    }

    return context.entries
}

private class CompileContext(
    var tokens: List<MarkdownToken>,
    var entries: MutableList<MarkdownAstEntry> = mutableListOf(),
    var codeLanguage: String? = null,
    var quoteLevel: Int = 0,
    var spaces: Int = 0
) {
    inline operator fun MarkdownAstEntry.unaryPlus() {
        entries += this
    }
}

private fun CompileContext.spaces(token: MarkdownToken, start: Int): Int {
    spaces = token.text.length
    return start + 1
}

private fun CompileContext.text(start: Int): Int {
    val last = entries.lastOrNull()

    val paragraph = if (last != null && last is MarkdownParagraphAstEntry && ! last.closed) {
        last
    } else {
        MarkdownParagraphAstEntry(mutableListOf(), false).also { entries += it }
    }

    return inline(start, paragraph.children)
}

private fun CompileContext.inline(start: Int, children: MutableList<MarkdownAstEntry>): Int {

    var index = start
    val end = tokens.size

    var activeStyle: String? = null
    var bold = false
    var italic = false

    fun style(token: MarkdownToken) {
        val text = token.text

        if (text == activeStyle) {
            activeStyle = null
            bold = false
            italic = false
            return
        }

        if (activeStyle != null) {
            children += MarkdownInlineAstEntry(text, bold, italic)
            return
        }

        when (text.length) {
            1 -> {
                italic = true
                activeStyle = text
            }

            2 -> {
                bold = true
                activeStyle = text
            }

            3 -> {
                italic = true
                bold = true
                activeStyle = text
            }

            else -> {
                children += MarkdownInlineAstEntry(text, bold, italic)
            }
        }
    }

    while (index < end) {
        val token = tokens[index ++]

        when (token.type) {

            MarkdownTokenType.Text, MarkdownTokenType.Hyphens -> {
                children += MarkdownInlineAstEntry(token.text, bold, italic)
            }

            MarkdownTokenType.Asterisks, MarkdownTokenType.Underscores -> {
                style(token)
            }

            MarkdownTokenType.CodeSpan -> {
                children += MarkdownInlineAstEntry(token.text, bold, italic, code = true)
            }

            MarkdownTokenType.InlineLink -> {
                children += MarkdownInlineAstEntry(token.text, bold, italic, inlineLink = true)
            }

            MarkdownTokenType.ReferenceLink -> {
                children += MarkdownInlineAstEntry(token.text, bold, italic, referenceLink = true)
            }

            MarkdownTokenType.ReferenceDef -> {
                children += MarkdownInlineAstEntry(token.text, bold, italic, referenceDef = true)
            }

            MarkdownTokenType.NewLine -> break

            else -> throw IllegalStateException("token type ${token.type} should not appear in inline context")
        }
    }

    return index
}

private fun CompileContext.newLine(start: Int): Int {
    val last = entries.lastOrNull()

    if (last != null && last is MarkdownParagraphAstEntry) {
        last.closed = true
    }

    return start + 1
}

private fun CompileContext.header(token: MarkdownToken, start: Int): Int {
    spaces = 0 // consume spaces before the header if any

    val children = mutableListOf<MarkdownAstEntry>()
    val next = inline(start + 1, children)

    + MarkdownHeaderAstEntry(
        level = token.text.length,
        children
    )

    return next
}

private fun CompileContext.list(token: MarkdownToken, start: Int): Int {
    val last = entries.lastOrNull()

    val bullet = (token.type == MarkdownTokenType.BulletList)

    val level =
        if (last != null && last is MarkdownListAstEntry && last.bullet == bullet) {
            when {
                last.spaces == spaces -> last.level
                last.spaces < spaces -> last.level + 1
                else -> last.level - 1
            }
        } else {
            1
        }

    check(level > 0) { "markdown parser error, list level should never be less than 0" }

    val children = mutableListOf<MarkdownAstEntry>()
    val next = inline(start + 1, children)

    + MarkdownListAstEntry(bullet, level, spaces, children)

    return next
}

private fun CompileContext.quote(start: Int): Int {
    TODO()
}

private fun CompileContext.codeLanguage(token: MarkdownToken, start: Int): Int {
    codeLanguage = token.text.trim()
    return start + 1
}

private fun CompileContext.codeFence(token: MarkdownToken, start: Int): Int {
    entries += MarkdownCodeFenceAstEntry(codeLanguage, token.text)
    codeLanguage = null
    return start + 1
}

private fun CompileContext.maybeRule(token: MarkdownToken, start: Int): Int {
    val index = start + 1
    if (index < tokens.size && token.text.length >= 3 && tokens[index].type == MarkdownTokenType.NewLine) {
        + MarkdownHorizontalRuleAstEntry()
        return index + 1
    } else {
        return text(start)
    }
}