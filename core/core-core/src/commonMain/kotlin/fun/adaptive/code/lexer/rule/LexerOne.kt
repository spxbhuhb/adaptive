package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer

class LexerOne(
    val rules: List<LexerRule>
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        var bestMatch: LexerRule? = null
        var bestMatchSize = Lexer.NO_MATCH

        for (rule in rules) {
            val matchSize = rule.match(source, start)

            if (matchSize <= bestMatchSize) continue

            bestMatch = rule
            bestMatchSize = matchSize
        }

        return if (bestMatch != null) bestMatchSize else Lexer.NO_MATCH
    }

}