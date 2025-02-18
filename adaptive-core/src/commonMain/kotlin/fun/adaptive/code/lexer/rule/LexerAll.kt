package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer

class LexerAll(
    val rules: List<LexerRule>
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        var current = start

        for (rule in rules) {
            val match = rule.match(source, current)
            
            if (match == Lexer.NO_MATCH) return Lexer.NO_MATCH

            current += match
        }

        return current - start
    }
}