/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.xml

internal enum class TokenType {
    STag,
    ETag,
    EmptyElemTag,
    Content,
    Other,
}

internal data class Token(
    val type: TokenType,
    val value: String
)

internal fun tokenizeXml(source: String): List<Token> {

    val tokens = mutableListOf<Token>()

    val chars = source.toCharArray()
    val end = chars.size
    var index = 0
    var start = 0

    while (index < end) {
        val char = chars[index]

        when (char) {
            '<' -> {
                if (start != index) {
                    tokens += Token(TokenType.Content, chars.concatToString(start, index))
                }

                start = ++index

                val type = when (chars[index]) {
                    '!' -> TokenType.Other
                    '?' -> TokenType.Other
                    '/' -> {
                        start = index + 1 // skip the '/'
                        TokenType.ETag
                    }
                    else -> TokenType.STag
                }

                while (index < end && chars[index] != '>') {
                    index ++
                }

                check(index != end) { "missing '>'" }

                if (chars[index - 1] == '/') {
                    check(type == TokenType.STag) { "Invalid XML structure at $index" }
                    tokens += Token(TokenType.EmptyElemTag, chars.concatToString(start, index ))
                } else {
                    tokens += Token(type, chars.concatToString(start, index))
                }

                start = index + 1
            }
        }

        index++
    }

    return tokens
}
