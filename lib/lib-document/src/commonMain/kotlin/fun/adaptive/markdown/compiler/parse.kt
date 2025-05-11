/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.markdown.model.*

internal fun parseInternal(tokens: List<MarkdownToken>): List<MarkdownElement> {

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
            MarkdownTokenType.ImageLink -> context.text(index)
            MarkdownTokenType.ReferenceLink -> context.text(index)
            MarkdownTokenType.ReferenceDef -> context.text(index)
        }
    }

    return context.entries
}

private class ListStackEntry(
    val spaces: Int,
    val list: MarkdownList
)

private class CompileContext(
    var tokens: List<MarkdownToken>,
    var entries: MutableList<MarkdownElement> = mutableListOf()
) {
    var codeLanguage: String? = null
    var listStack = mutableListOf<ListStackEntry>()
    var spaces = 0

    operator fun MarkdownElement.unaryPlus() {
        entries += this
    }
}

private fun CompileContext.spaces(token: MarkdownToken, start: Int): Int {
    spaces = token.text.length
    return start + 1
}

private fun CompileContext.text(start: Int): Int {
    val last = entries.lastOrNull()

    val paragraph = if (last != null && last is MarkdownParagraph && ! last.closed) {
        last.children += MarkdownInline(" ", bold = false, italic = false) // TODO think about in-paragraph newline in markdown
        last
    } else {
        MarkdownParagraph(mutableListOf(), false).also { entries += it }
    }

    return inline(start, paragraph.children)
}

private fun CompileContext.inline(start: Int, children: MutableList<MarkdownElement>): Int {

    var index = start
    val end = tokens.size

    var activeStyle: String? = null
    var bold = false
    var italic = false

    fun style(text: String) {
        if (text == activeStyle || (text.length == 3 && activeStyle?.length == 3)) {
            activeStyle = null
            bold = false
            italic = false
            return
        }

        if (activeStyle != null) {
            children += MarkdownInline(text, bold, italic)
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
                children += MarkdownInline(text, bold, italic)
            }
        }
    }

    fun mergeStyles(token : MarkdownToken) : String {
        if (index == end) return token.text
        val next = tokens[index]
        if (token.type == MarkdownTokenType.Asterisks) {
            if (next.type == MarkdownTokenType.Underscores) {
                index++
                return token.text + next.text
            }
        } else {
            if (next.type == MarkdownTokenType.Asterisks) {
                index++
                return token.text + next.text
            }
        }
        return token.text
    }

    while (index < end) {
        val token = tokens[index ++]

        when (token.type) {

            MarkdownTokenType.Text, MarkdownTokenType.Hyphens -> {
                children += MarkdownInline(token.text, bold, italic)
            }

            MarkdownTokenType.Asterisks, MarkdownTokenType.Underscores -> {
                style(mergeStyles(token))
            }

            MarkdownTokenType.CodeSpan -> {
                children += MarkdownInline(token.text, bold, italic, code = true)
            }

            MarkdownTokenType.ImageLink -> {
                children += MarkdownInline(token.text, bold, italic, imageLink = true)
            }

            MarkdownTokenType.InlineLink -> {
                children += MarkdownInline(token.text, bold, italic, inlineLink = true)
            }

            MarkdownTokenType.ReferenceLink -> {
                children += MarkdownInline(token.text, bold, italic, referenceLink = true)
            }

            MarkdownTokenType.ReferenceDef -> {
                children += MarkdownInline(token.text, bold, italic, referenceDef = true)
            }

            MarkdownTokenType.NewLine -> break

            else -> throw IllegalStateException("token type ${token.type} should not appear in inline context")
        }
    }

    return index
}

private fun CompileContext.newLine(start: Int): Int {
    val last = entries.lastOrNull()

    if (last != null) {
        when (last) {
            is MarkdownParagraph -> last.closed = true
            is MarkdownList -> listStack.clear()
        }
    }

    spaces = 0

    return start + 1
}

private fun CompileContext.header(token: MarkdownToken, start: Int): Int {
    spaces = 0 // consume spaces before the header if any

    val children = mutableListOf<MarkdownElement>()
    val next = inline(start + 1, children)

    + MarkdownHeader(
        level = token.text.length,
        children
    )

    return next
}

private fun CompileContext.list(token: MarkdownToken, start: Int): Int {
    val bullet = (token.type == MarkdownTokenType.BulletList)

    // Remove lists from the stack which are deeper than this token
    // the last stack item is a list which is at the same level as
    // this token.

    while (listStack.isNotEmpty() && spaces < listStack.last().spaces) {
        listStack.removeLast()
    }

    // The inline content of the list item. This cannot contain other lists, it
    // is just a paragraph.

    val children = mutableListOf<MarkdownElement>()
    val next = inline(start + 1, children)
    val paragraph = MarkdownParagraph(children, false)

    if (listStack.isNotEmpty()) {
        // We have a list on the stack. We might make a new sub-list or append
        // the list item to the list on the stack, depending on the level of the item.

        val last = listStack.last()
        val currentList = last.list

        if (last.spaces == spaces) {
            currentList.items += MarkdownListItem(bullet, listStack.size, paragraph)
        } else {
            val subListItem = MarkdownListItem(bullet, listStack.size + 1, paragraph)
            val subList = MarkdownList(bullet, subListItem.level, mutableListOf(subListItem))

            currentList.items.last().subList = subList

            listStack += ListStackEntry(spaces, subList)
        }
    } else {
        // There is no list on the stack, so we have to make a new one
        // The sole item in this list will be the paragraph we've just made.

        + MarkdownList(bullet, 1, mutableListOf(MarkdownListItem(bullet, 1, paragraph)))
            .also { listStack += ListStackEntry(spaces, it) }
    }

    spaces = 0

    return next
}

private fun CompileContext.quote(token: MarkdownToken, start: Int): Int {

    + MarkdownQuote(parseInternal(tokenizeInternal(token.text)).toMutableList())

    return start + 1
}

private fun CompileContext.codeLanguage(token: MarkdownToken, start: Int): Int {
    codeLanguage = token.text.trim()
    return start + 1
}

private fun CompileContext.codeFence(token: MarkdownToken, start: Int): Int {
    entries += MarkdownCodeFence(codeLanguage, token.text)
    codeLanguage = null
    return start + 1
}

private fun CompileContext.maybeRule(token: MarkdownToken, start: Int): Int {
    val index = start + 1
    val end = tokens.size

    if (token.text.length >= 3 && index == end || (index < tokens.size && tokens[index].type == MarkdownTokenType.NewLine)) {
        + MarkdownHorizontalRule()
        return index + 1
    } else {
        return text(start)
    }
}