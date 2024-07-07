/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown.parse

interface MarkdownAstEntry

data class MarkdownHeaderAstEntry(
    val level: Int,
    val children: List<MarkdownInlineAstEntry>
) : MarkdownAstEntry

data class MarkdownInlineAstEntry(
    val text: String,
    val bold: Boolean,
    val italic: Boolean,
    val code: Boolean = false,
    val inlineLink: Boolean = false,
    val referenceLink: Boolean = false,
    val referenceDef: Boolean = false,
) : MarkdownAstEntry

data class MarkdownParagraphAstEntry(
    val children: MutableList<MarkdownInlineAstEntry>,
    var closed: Boolean
) : MarkdownAstEntry

data class MarkdownListAstEntry(
    val bullet: Boolean,
    val level: Int,
    val children: List<MarkdownAstEntry>
) : MarkdownAstEntry

data class MarkdownCodeFenceAstEntry(
    val language: String?,
    val content: String
) : MarkdownAstEntry

data class MarkdownQuoteEntry(
    val children: List<MarkdownAstEntry>
) : MarkdownAstEntry

class MarkdownHorizontalRuleAstEntry : MarkdownAstEntry {

    override fun toString(): String {
        return this::class.simpleName.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

fun ast(tokens: List<MarkdownToken>): List<MarkdownAstEntry> {

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
            MarkdownTokenType.Quote -> context.quote(token, index)
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
    var entries: MutableList<MarkdownAstEntry> = mutableListOf()
) {
    var codeLanguage: String? = null
    var listStack = mutableListOf<Int>()
    var spaces = 0

    operator fun MarkdownAstEntry.unaryPlus() {
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
        last.children += MarkdownInlineAstEntry(" ", bold = false, italic = false) // TODO think about in-paragraph newline in markdown
        last
    } else {
        MarkdownParagraphAstEntry(mutableListOf(), false).also { entries += it }
    }

    return inline(start, paragraph.children)
}

private fun CompileContext.inline(start: Int, children: MutableList<MarkdownInlineAstEntry>): Int {

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

    spaces = 0

    return start + 1
}

private fun CompileContext.header(token: MarkdownToken, start: Int): Int {
    spaces = 0 // consume spaces before the header if any

    val children = mutableListOf<MarkdownInlineAstEntry>()
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

    if (last != null && last is MarkdownListAstEntry && last.bullet != bullet) {
        listStack.clear()
    }

    while (listStack.isNotEmpty() && spaces <= listStack.last()) {
        listStack.removeLast()
    }
    listStack.add(spaces)

    val level = listStack.size
    spaces = 0

    val children = mutableListOf<MarkdownInlineAstEntry>()
    val next = inline(start + 1, children)

    + MarkdownListAstEntry(bullet, level, children)

    return next
}

private fun CompileContext.quote(token: MarkdownToken, start: Int): Int {

    + MarkdownQuoteEntry(ast(tokenize(token.text)))

    return start + 1
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
    val end = tokens.size

    if (token.text.length >= 3 && index == end || (index < tokens.size && tokens[index].type == MarkdownTokenType.NewLine)) {
        + MarkdownHorizontalRuleAstEntry()
        return index + 1
    } else {
        return text(start)
    }
}