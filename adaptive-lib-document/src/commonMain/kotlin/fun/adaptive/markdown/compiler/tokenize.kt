/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.utility.*
import kotlin.math.max

internal fun tokenizeInternal(source: String): List<MarkdownToken> {

    var index = 0
    val end = source.length
    val tokens = mutableListOf<MarkdownToken>()

    while (index < end) {
        index = line(source, index, end, tokens)
    }

    return tokens
}

private fun line(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    // if there are only spaces in the last line this will drop them, but that's fine
    val index = source.firstNotSpaceOrNull(start, end) ?: return end
    if (index != start) {
        tokens += MarkdownToken(MarkdownTokenType.Spaces, source.substring(start, index))
    }

    val char = source[index]

    return when (char) {
        '#' -> header(source, index, end, tokens)
        '*' -> maybeList(source, index, end, tokens)
        '-' -> maybeList(source, index, end, tokens)
        '>' -> quote(source, index, end, tokens)
        '`' -> backtick(source, index, end, tokens)
        in ('0' .. '9') -> number(source, index, end, tokens)
        '\r', '\n', '\u2028', '\u2029' -> newLine(source, index, end, tokens)
        else -> text(source, index, end, tokens)
    }
}

private fun header(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    val hashEnd = source.firstNotOrNull(start, end) { it == '#' } ?: return end

    tokens += MarkdownToken(MarkdownTokenType.Header, source.substring(start, hashEnd))

    val textStart = source.firstNotSpace(hashEnd, end)

    return text(source, textStart, end, tokens)
}

private fun maybeList(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    var index = start + 1

    if (index < end && source[index].isSpace()) {
        tokens += MarkdownToken(MarkdownTokenType.BulletList, "*")
        index ++ // skip the first space
    } else {
        index = start
    }

    return text(source, index, end, tokens)
}

private val quoteLineRegex = "\\s?>([^\\n\\r]*)[\\n\\r]?".toRegex()

private fun quote(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    var index = start
    val quoteContent = StringBuilder()

    while (index < end) {
        val lineEnd = source.first(index, end) { it.isNewLine() }

        quoteContent.append(source.substring(index + 1, lineEnd))
        quoteContent.append('\n')

        index = lineEnd + 1 // character after the new line character
        if (index >= end) break

        // we are standing on the next character that may be:
        // - a new line character different from the one at source[lineEnd] - i.e. \r\n
        // - a new line character that is the same as the one at source[lineEnd] - i.e. \n\n
        // - a quote character that means there is another line in the quote
        // - some spaces and a quote character

        val lineEndChar = source[lineEnd]
        val nextChar = source[index]

        if (nextChar == lineEndChar) break // covers \n\n

        if (nextChar.isNewLine()) { // covers \r\n
            index++
            if (index >= end) break
        }

        val nextNonSpace = source.firstNotSpace(index, end)
        if (nextNonSpace >= end || source[nextNonSpace] != '>') break

        index = nextNonSpace
    }

    tokens += MarkdownToken(MarkdownTokenType.Quote, quoteContent.toString().trimEnd())

    return index
}


private fun number(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    var index = source.firstNot(start, end) { it.isDigit() }

    if (index + 1 < end && source[index] == '.' && source[index + 1].isSpace()) {
        tokens += MarkdownToken(MarkdownTokenType.NumberedList, source.substring(start, index))
        index += 2 // skip the '.' and the following space
    } else {
        index = start
    }

    return text(source, index, end, tokens)
}

val codeFenceRegex = "(```|~~~)\\s*(\\w+)?\\s*\\n([\\s\\S]*?)\\n\\1".toRegex()

private fun backtick(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    codeFenceRegex.matchAt(source, start)?.let {
        val language = it.groupValues[2]
        val content = it.groupValues[3]

        if (language.isNotEmpty()) {
            tokens += MarkdownToken(MarkdownTokenType.CodeLanguage, language)
        }

        tokens += MarkdownToken(MarkdownTokenType.CodeFence, content)

        return it.range.last + 1
    }

    return text(source, start, end, tokens)
}

private fun newLine(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    var current = start
    var nl = 0
    var cr = 0

    while (current < end) {
        when (source[current]) {
            '\n' -> nl ++
            '\r' -> cr ++
            else -> break
        }
        current ++
    }

    repeat(max(nl, cr)) {
        tokens += MarkdownToken(MarkdownTokenType.NewLine, "")
    }

    return current
}

private fun text(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    var index = start
    val builder = StringBuilder()

    fun emptyBuilder() {
        if (builder.isNotEmpty()) {
            tokens += MarkdownToken(MarkdownTokenType.Text, builder.toString())
            builder.clear()
        }
    }

    fun maybeReplace(char: Char, maybeIndex: Int) {
        if (maybeIndex == index) {
            builder.append(char)
            index ++
        } else {
            val token = tokens.removeLast()
            emptyBuilder()
            tokens += token
            index = maybeIndex
        }
    }

    fun sameChar(char: Char, type: MarkdownTokenType) {
        emptyBuilder()

        val not = source.firstNot(index, end) { it == char }
        tokens += MarkdownToken(type, source.substring(index, not))

        index = not
    }

    while (index < end) {
        val char = source[index]

        when (char) {
            '*' -> sameChar('*', MarkdownTokenType.Asterisks)
            '-' -> sameChar('-', MarkdownTokenType.Hyphens)
            '_' -> sameChar('_', MarkdownTokenType.Underscores)

            '`' -> {
                emptyBuilder()
                index = code(source, index, end, tokens)
            }

            '!' -> {
                val maybeIndex = maybeImage(source, index, tokens)
                maybeReplace(char, maybeIndex)
            }

            '[' -> {
                val maybeIndex = maybeReference(source, index, tokens)
                maybeReplace(char, maybeIndex)
            }

            '\\' -> {
                val nextChar = source[index + 1]

                if (nextChar in "\\`*-{}[]<>()#_+.!") {
                    builder.append(nextChar)
                    index ++
                } else {
                    builder.append(char)
                }
            }

            '\r', '\n', '\u2028', '\u2029' -> {
                emptyBuilder()
                return index
            }

            else -> {
                builder.append(char)
                index ++
            }
        }
    }

    emptyBuilder()
    return end
}

private val codeRegex = "(`{1,2}) ?([^`]*?[^\\\\])? ?\\1".toRegex()

private fun code(source: String, start: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    val matchResult = codeRegex.matchAt(source, start)

    if (matchResult == null) {
        tokens += MarkdownToken(MarkdownTokenType.CodeSpan, source.substring(start, end))
        return end
    }

    val ticks = matchResult.groupValues[1]
    val rawText = matchResult.groupValues[2].replace("\\`", "`").replace("\\\\", "\\")

    val text = if (ticks.length == 2) {
        rawText.trim()
    } else {
        rawText
    }

    tokens += MarkdownToken(MarkdownTokenType.CodeSpan, text)

    return matchResult.range.last + 1
}

private val imageLinkRegex = "!\\[[^\\[]+\\]\\([^)]+\\)".toRegex()

private fun maybeImage(source: String, start: Int, tokens: MutableList<MarkdownToken>): Int {

    imageLinkRegex.matchAt(source, start)?.let {
        tokens += MarkdownToken(MarkdownTokenType.InlineLink, it.value)
        return it.range.last + 1
    }

    return start
}

private val inlineLinkRegex = "\\[[^\\[]+\\]\\([^)]+\\)".toRegex()
private val referenceLinkRegex = "\\[[^\\[]+\\]\\[[^\\]]+\\]".toRegex()
private val referenceDefRegex = "^\\[[^\\[]+\\]:\\s*(.+)$".toRegex(RegexOption.MULTILINE)

private fun maybeReference(source: String, start: Int, tokens: MutableList<MarkdownToken>): Int {

    inlineLinkRegex.matchAt(source, start)?.let {
        tokens += MarkdownToken(MarkdownTokenType.InlineLink, it.value)
        return it.range.last + 1
    }

    referenceLinkRegex.matchAt(source, start)?.let {
        tokens += MarkdownToken(MarkdownTokenType.ReferenceLink, it.value)
        return it.range.last + 1
    }

    referenceDefRegex.matchAt(source, start)?.let {
        tokens += MarkdownToken(MarkdownTokenType.ReferenceDef, it.value)
        return it.range.last + 1
    }

    return start
}