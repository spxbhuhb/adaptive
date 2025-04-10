/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.xml

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

internal fun tokenize(source: String): List<Token> {

    val tokens = mutableListOf<Token>()

    val end = source.length
    var index = 0
    var start = 0

    while (index < end) {
        val char = source[index]

        when (char) {
            '<' -> {
                if (start != index) {
                    tokens += Token(TokenType.Content, source.substring(start, index))
                }

                start = ++index

                val type = when (source[index]) {
                    '!' -> TokenType.Other
                    '?' -> TokenType.Other
                    '/' -> {
                        start = index + 1 // skip the '/'
                        TokenType.ETag
                    }
                    else -> TokenType.STag
                }

                while (index < end && source[index] != '>') {
                    index ++
                }

                check(index != end) { "missing '>'" }

                if (source[index - 1] == '/') {
                    check(type == TokenType.STag) { "Invalid XML structure at $index" }
                    tokens += Token(TokenType.EmptyElemTag, source.substring(start, index ))
                } else {
                    tokens += Token(type, source.substring(start, index))
                }

                start = index + 1
            }
        }

        index++
    }

    return tokens
}
