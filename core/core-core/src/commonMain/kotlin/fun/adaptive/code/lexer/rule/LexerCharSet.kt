package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import `fun`.adaptive.code.lexer.rule.LexerRule

class LexerCharSet(
    val chars: CharArray
) : LexerRule() {
    override fun match(source: CharArray, start: Int): Int {
        if (start >= source.size) return Lexer.NO_MATCH
        if (source[start] in chars) return 1
        return Lexer.NO_MATCH
    }
}
