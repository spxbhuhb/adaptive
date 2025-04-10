package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer

/**
 * Matches any characters before the next occurrence of an array of characters such as `'abc'`.
 */
class LexerReverseStringLiteral(
    val chars: CharArray
) : LexerRule() {

    init {
        check(chars.isNotEmpty()) { "The array of characters must not be empty." }
    }

    override fun match(source: CharArray, start: Int): Int {
        var offset = start
        val end = source.size
        val first = chars[0]

        if (start >= end) return Lexer.NO_MATCH

        while (offset < end) {

            if (source[offset] != first) {
                offset ++
                continue
            }

            var mismatch = false

            if (offset + chars.size > end) return end - start

            for (i in 1 until chars.size) {
                if (source[offset + i] != chars[i]) {
                    mismatch = true
                    break
                }
            }

            if (!mismatch) return offset - start

            offset ++
        }

        return end - start
    }

}