package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import `fun`.adaptive.code.lexer.rule.LexerRule

class LexerReverseSingleChar(
    val char: Char
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        if (start >= source.size) return Lexer.NO_MATCH
        if (source[start] == char) return Lexer.NO_MATCH
        return 1
    }

}