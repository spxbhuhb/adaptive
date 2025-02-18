package `fun`.adaptive.code.parser

import `fun`.adaptive.code.lexer.LexerTokenDef
import `fun`.adaptive.code.parser.rule.ParserAll
import `fun`.adaptive.code.parser.rule.ParserMany
import `fun`.adaptive.code.parser.rule.ParserOne
import `fun`.adaptive.code.parser.rule.ParserRule
import `fun`.adaptive.code.parser.rule.ParserSingleToken

class ParserBuilder {

    val rules = mutableListOf<ParserRule>()

    fun all(buildFun: ParserBuilder.() -> Unit) {
        rules += ParserAll(ParserBuilder().apply(buildFun).rules)
    }

    fun one(buildFun: ParserBuilder.() -> Unit) {
        rules += ParserOne(ParserBuilder().apply(buildFun).rules)
    }

    fun many(min : Int = 0, max: Int = Int.MAX_VALUE, buildFun: ParserBuilder.() -> Unit) {
        rules += ParserMany(
            ParserBuilder().apply(buildFun).rules.single(),
            min = min,
            max = max
        )
    }

    fun optional(buildFun: ParserBuilder.() -> Unit) {
        rules += ParserMany(
            ParserBuilder().apply(buildFun).rules.single(),
            min = 0,
            max = 1
        )
    }

    operator fun ParserRule.unaryPlus() {
        this@ParserBuilder.rules += this
    }

    operator fun LexerTokenDef.unaryPlus() {
        this@ParserBuilder.rules += ParserSingleToken(this)
    }

}
