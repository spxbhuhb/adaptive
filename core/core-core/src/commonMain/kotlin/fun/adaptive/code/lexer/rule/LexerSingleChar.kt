package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer

class LexerSingleChar(
    val char: Char
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        if (start >= source.size) return Lexer.NO_MATCH
        if (source[start] == char) return 1
        return Lexer.NO_MATCH
    }

}