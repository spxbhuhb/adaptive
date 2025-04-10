package `fun`.adaptive.code.lexer

import `fun`.adaptive.code.lexer.rule.LexerRule
import `fun`.adaptive.code.lexer.rule.*

class LexerBuilder {

    val rules = mutableListOf<LexerRule>()

    fun char(valueFun : () -> Char) {
        rules += LexerSingleChar(valueFun())
    }

    fun string(valueFun: () -> String) {
        rules += LexerStringLiteral(valueFun().toCharArray())
    }

    fun charRange(valueFun : () -> CharRange) {
        rules += valueFun().let { LexerCharRange(it.start, it.endInclusive) }
    }

    fun charSet(valueFun : () -> String) {
        rules += LexerCharSet(valueFun().toCharArray())
    }

    fun allBefore(ruleFun: () -> Unit) {
        ruleFun()

        val rule = rules.removeLast()

        when (rule) {
            is LexerSingleChar -> LexerReverseSingleChar(rule.char)
            is LexerCharRange -> LexerReverseCharRange(rule.from, rule.to)
            is LexerCharSet -> LexerReverseCharSet(rule.chars)
            is LexerStringLiteral -> LexerReverseStringLiteral(rule.chars)
            else -> throw IllegalArgumentException("Unsupported rule type: ${rule::class.simpleName}")
        }.also {
            rules += it
        }
    }

    fun all(buildFun: LexerBuilder.() -> Unit) {
        rules += LexerAll(LexerBuilder().apply(buildFun).rules)
    }

    fun one(buildFun: LexerBuilder.() -> Unit) {
        rules += LexerOne(LexerBuilder().apply(buildFun).rules)
    }

    fun many(
        min : Int = 0,
        max: Int = Int.MAX_VALUE,
        buildFun: LexerBuilder.() -> Unit
    ) {
        rules += LexerMany(
            LexerBuilder().apply(buildFun).rules.single(),
            min = min,
            max = max
        )
    }

    operator fun LexerRule.unaryPlus() {
        this@LexerBuilder.rules += this
    }
}
