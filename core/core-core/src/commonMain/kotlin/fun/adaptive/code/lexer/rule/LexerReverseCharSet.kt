package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer

class LexerReverseCharSet(
    val chars: CharArray
) : LexerRule() {
    override fun match(source: CharArray, start: Int): Int {
        if (start >= source.size) return Lexer.NO_MATCH

        var current = start
        val end = source.size

        while (current < end) {
            if (source[current] in chars) break
            current++
        }

        return current - start
    }
}