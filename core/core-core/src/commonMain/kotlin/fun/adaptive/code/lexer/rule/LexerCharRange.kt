package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import `fun`.adaptive.code.lexer.rule.LexerRule

class LexerCharRange(
    val from: Char,
    val to: Char
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        if (start >= source.size) return Lexer.NO_MATCH
        if (source[start] in from .. to) return 1
        return Lexer.NO_MATCH
    }

}