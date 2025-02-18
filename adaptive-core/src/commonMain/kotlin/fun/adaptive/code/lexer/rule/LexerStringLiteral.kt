package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import `fun`.adaptive.code.lexer.rule.LexerRule

/**
 * An array of characters such as `'abc'`.
 */
class LexerStringLiteral(
    val chars: CharArray
) : LexerRule() {


    override fun match(source: CharArray, start: Int): Int {
        if (chars.size > source.size - start) return Lexer.NO_MATCH

        var offset = start
        for (char in chars) {
            if (char != source[offset ++]) return Lexer.NO_MATCH
        }

        return chars.size
    }

}