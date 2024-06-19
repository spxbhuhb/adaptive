/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown

import hu.simplexion.adaptive.utility.*
import kotlin.math.max

enum class MarkdownTokenType {
    Header,
    BulletList,
    NumberedList,
    Text,
    Quote,
    CodeFence,
    NewLine,
    Bold,
    HorizontalRule
}

class MarkdownToken(
    val type: MarkdownTokenType,
    val spaces: Int,
    val text: String
)

fun tokenize(source: String): List<MarkdownToken> {

    var index = 0
    val end = source.length
    val tokens = mutableListOf<MarkdownToken>()

    while (index < end) {
        index = line(source, index, end, tokens)
    }

    return tokens
}

private fun line(source: String, index: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    // if there are only spaces in the last line this will drop them, but that's fine
    val notSpaceIndex = source.firstNotSpaceOrNull(index, end) ?: return end

    val char = source[notSpaceIndex]

    return when (char) {
        '#' -> hash(source, index, notSpaceIndex, end, tokens)
        '*' -> maybeList(source, index, notSpaceIndex, end, tokens)
        '-' -> maybeList(source, index, notSpaceIndex, end, tokens)
        '>' -> greaterThan(source, index, notSpaceIndex, end, tokens)
        '`' -> backtick(source, index, notSpaceIndex, end, tokens)
        in ('0' .. '9') -> number(source, index, notSpaceIndex, end, tokens)
        '\r', '\n', '\u2028', '\u2029' -> newLine(source, index, notSpaceIndex, end, tokens)
        else -> text(source, index, end, notSpaceIndex, tokens)
    }
}


private fun hash(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    val textStart = source.firstNotOrNull(notSpaceIndex, end) { it == '#' } ?: return end

    tokens += MarkdownToken(
        MarkdownTokenType.Header,
        notSpaceIndex - index,
        source.substring(notSpaceIndex, textStart)
    )

    return text(
        source, textStart,
        source.firstNotSpace(textStart, end),
        end, tokens
    )
}

private fun maybeList(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    val nextIndex = notSpaceIndex + 1

    if (nextIndex < end && source[nextIndex].isSpace()) {

        tokens += MarkdownToken(
            MarkdownTokenType.BulletList,
            notSpaceIndex - index,
            "*"
        )

        return text(
            source, nextIndex,
            source.firstNotSpace(nextIndex, end),
            end, tokens
        )

    } else {
        return text(
            source, index,
            notSpaceIndex,
            end, tokens
        )
    }
}

private fun greaterThan(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    val nextIndex = notSpaceIndex + 1

    tokens += MarkdownToken(
        MarkdownTokenType.Quote,
        notSpaceIndex - index,
        source.substring(index, nextIndex)
    )

    return line(source, nextIndex, end, tokens)
}

private fun number(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    val nextIndex = source.firstNot(notSpaceIndex, end) { it.isDigit() }

    if (nextIndex < end && source[nextIndex] == '.') {

        tokens += MarkdownToken(
            MarkdownTokenType.NumberedList,
            notSpaceIndex - index,
            source.substring(notSpaceIndex, nextIndex)
        )

        return text(
            source, nextIndex,
            source.firstNotSpace(nextIndex, end),
            end, tokens
        )

    } else {
        return text(
            source, index,
            notSpaceIndex,
            end, tokens
        )
    }
}

private fun backtick(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    val nextIndex = notSpaceIndex + 2

    if (nextIndex < end && source[nextIndex] == '`' && source[nextIndex - 1] == '`') {

        var fenceEnd: Int = end
        var lastTry: Int = nextIndex + 1

        while (lastTry < end) {
            val maybeFenceEnd = source.indexOf("```", lastTry)
            if (maybeFenceEnd == - 1 || maybeFenceEnd == end - 3) break

            val firstNotSpaceBefore = source.firstNotSpaceBefore(start = maybeFenceEnd - 1, until = lastTry)
            val firstNotSpaceAfter = source.firstNotSpace(start = maybeFenceEnd + 3, end)

            val previousChar = source[firstNotSpaceBefore]
            val nextChar = source[firstNotSpaceAfter]

            if ((previousChar == '\r' || previousChar == '\n') && (nextChar == '\r' || nextChar == '\n')) {
                fenceEnd = maybeFenceEnd
                break
            }

            lastTry = maybeFenceEnd
        }

        tokens += MarkdownToken(
            MarkdownTokenType.CodeFence,
            notSpaceIndex - index,
            source.substring(notSpaceIndex + 3, fenceEnd)
        )

        return fenceEnd

    } else {
        return text(
            source, index,
            notSpaceIndex,
            end, tokens
        )
    }
}

private fun newLine(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {

    var current = index
    var nl = 0
    var cr = 0

    while (current < end) {
        when (source[current ++]) {
            '\n' -> nl ++
            '\r' -> cr ++
            else -> break
        }
    }

    repeat(max(nl, cr)) {
        tokens += MarkdownToken(
            MarkdownTokenType.NewLine,
            notSpaceIndex - index,
            ""
        )
    }

    return current
}

private fun text(source: String, index: Int, notSpaceIndex: Int, end: Int, tokens: MutableList<MarkdownToken>): Int {
    TODO()
}