package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import `fun`.adaptive.code.lexer.rule.LexerRule

class LexerMany(
    val rule: LexerRule,
    val min: Int = 0,
    val max: Int = Int.MAX_VALUE
) : LexerRule() {

    override fun match(source: CharArray, start: Int): Int {
        var current = start
        val end = source.size

        var matchSize = 0
        var count = 0

        while (current < end && count < max) {
            val match = rule.match(source, current)
            if (match == Lexer.NO_MATCH) break
            if (match == 0) break // this means a reverse match
            current += match
            matchSize += match
            count ++
        }

        return if (count < min) Lexer.NO_MATCH else matchSize
    }
}